package org.alexside.vaadin.misc;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.Action;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.Category;
import org.alexside.entity.Notice;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.enums.TreeKind;
import org.alexside.events.FilterByTagEvent;
import org.alexside.events.TItemRefreshEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.DataProvider;
import org.alexside.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.alexside.utils.ThemeUtils.HEADER_BUTTON;
import static org.alexside.utils.ThemeUtils.PANEL_SCROLLABLE;

/**
 * Created by abalyshev on 27.10.16.
 */
@SpringComponent
@ViewScope
public class KibiTree extends KibiPanel {

    @Autowired
    protected DataProvider dataProvider;

    protected EventBus eventBus;

    protected Tree tree;

    protected Action addCategory = new Action("Добавить категорию", FontAwesome.FOLDER);
    protected Action addNotice = new Action("Добавить запись", FontAwesome.EDIT);
    protected Action delete = new Action("Удалить", FontAwesome.CLOSE);

    @PostConstruct
    public void onInit() {
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        IconButton addButton = IconButton.addButton();
        addButton.addClickListener(event -> {
            TItem ti = new Category("Категория_" + Instant.now().toEpochMilli(), null);
            dataProvider.saveTItem(ti);
            Item added = addContainerItem(
                    (HierarchicalContainer)tree.getContainerDataSource(), ti);
            if (added == null) return;
            tree.expandItem(ti.getId());
            tree.setChildrenAllowed(ti.getId(), true);
        });

        List<TItem> data = dataProvider.getTreeData();
        HierarchicalContainer container = new HierarchicalContainer();
        container.addContainerProperty("id", String.class, "");
        container.addContainerProperty("name", String.class, "");
        container.addContainerProperty("content", String.class, "");
        container.addContainerProperty("icon", Resource.class, null);
        container.addContainerProperty("kind", TreeKind.class, TreeKind.UNKNOWN);
        container.addContainerProperty("object", TItem.class, null);
        data.forEach(tItem -> initContainer(container, tItem));
        sortContainer(container);

        TextField searchField = new TextField();
        searchField.setSizeFull();
        searchField.addStyleName(HEADER_BUTTON);
        searchField.addValueChangeListener(event -> {
            container.removeAllContainerFilters();
            String value = (String)event.getProperty().getValue();
            SimpleStringFilter filter = new SimpleStringFilter("content", value, true, false);
            container.addContainerFilter(filter);
        });

        IconButton clearButton = IconButton.clearButton();
        clearButton.addClickListener(event -> {
            container.removeAllContainerFilters();
        });

        HorizontalLayout searchWrap = new HorizontalLayout(clearButton, searchField);
        searchWrap.setSizeFull();
        searchWrap.setSpacing(true);
        searchWrap.setExpandRatio(searchField, 1);

        addToTopToolbar(searchWrap, Alignment.TOP_LEFT, 5);
        addToTopToolbar(addButton, Alignment.TOP_RIGHT, 1);

        tree = new Tree(null, container);
        tree.setSizeFull();
        tree.setImmediate(true);
        tree.addStyleName(PANEL_SCROLLABLE);
        tree.setItemIconPropertyId("icon");
        tree.setItemCaptionPropertyId("name");
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tree.addValueChangeListener(event -> {
            TItem selected = getSelected();
            if (selected != null) {
                EventUtils.post(new TItemSelectionEvent(selected));
            }
        });
        tree.setDragMode(Tree.TreeDragMode.NODE);
        tree.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable t = (DataBoundTransferable) dropEvent
                        .getTransferable();
                Container sourceContainer = t.getSourceContainer();
                Object sourceItemId = t.getItemId();
                Item sourceItem = sourceContainer.getItem(sourceItemId);
                TItem sourceTi  = (TItem) sourceItem.getItemProperty("object").getValue();

                AbstractSelect.AbstractSelectTargetDetails dropData = ((AbstractSelect.AbstractSelectTargetDetails) dropEvent
                        .getTargetDetails());
                Object targetItemId = dropData.getItemIdOver();
                Item targetItem = sourceContainer.getItem(targetItemId);
                TItem targetTi = (TItem) targetItem.getItemProperty("object").getValue();

                if (sourceTi != null || targetTi != null || targetTi.isCategory()) {
                    Set<TItem> savedItems = new HashSet<>();
                    container.setParent(sourceItemId, targetItemId);
                    TItem sourceParentTi = sourceTi.getParent();
                    if (sourceParentTi != null) {
                        sourceParentTi.getChildren().remove(sourceTi);
                        savedItems.add(sourceParentTi);
                    }
                    sourceTi.setParent(targetTi);
                    targetTi.getChildren().add(sourceTi);
                    savedItems.addAll(Arrays.asList(targetTi, sourceTi));
                    dataProvider.saveTItems(savedItems);
                }
            }
            @Override
            public AcceptCriterion getAcceptCriterion() { return AcceptAll.get(); }
        });

        tree.addActionHandler(new Action.Handler() {
            @Override
            public Action[] getActions(Object target, Object sender) {
                Item i = tree.getItem(target);
                if (i == null) return null;

                TreeKind kind = (TreeKind)i.getItemProperty("kind").getValue();
                if (kind == TreeKind.CATEGORY) {
                    return new Action[] { addCategory, addNotice, delete };
                } else if (kind == TreeKind.NOTICE) {
                    return new Action[] { delete };
                }
                return null;
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                Item i = tree.getItem(target);
                if (i == null) return;

                TItem selected = (TItem) i.getItemProperty("object").getValue();
                if (selected == null) return;

                if (action == addCategory) {
                    TItem ti = new Category("Категория_" + Instant.now().toEpochMilli(), (Category) selected);
                    // at first persist item to storage to get unique id
                    dataProvider.saveTItem(ti);
                    Item added = addContainerItem(
                            (HierarchicalContainer)tree.getContainerDataSource(), ti);
                    if (added == null) return;
                    tree.expandItem(ti.getId());
                    tree.setChildrenAllowed(ti.getId(), true);
                } else if (action == addNotice) {
                    TItem ti = new Notice("Запись_" + Instant.now().toEpochMilli(), "", (Category) selected);
                    dataProvider.saveTItem(ti);
                    Item added = addContainerItem(
                            (HierarchicalContainer)tree.getContainerDataSource(), ti);
                    if (added == null) return;
                    tree.setChildrenAllowed(ti.getId(), false);
                } else if (action == delete) {
                    dataProvider.removeTItem(selected);
                    removeItem((HierarchicalContainer)tree.getContainerDataSource(), target);
                }
                sortContainer((HierarchicalContainer) tree.getContainerDataSource());
            }
        });

        tree.getItemIds().forEach(i -> {
            TreeKind kind = (TreeKind)tree.getItem(i).getItemProperty("kind").getValue();
            tree.setChildrenAllowed(i, false);
            if (kind != null && kind == TreeKind.CATEGORY) {
                tree.setChildrenAllowed(i, true);
                tree.expandItem(i);
            }
        });

        setContentAlt(tree);
    }

    @PreDestroy
    public void onDestroy() {
        eventBus.unregister(this);
    }

    public TItem getSelected() {
        Item selected = tree.getItem(tree.getValue());
        if (selected == null) return null;
        return (TItem) selected.getItemProperty("object").getValue();
    }

    @Subscribe
    public void onTItemRefreshEvent(TItemRefreshEvent event) {
        if (event.getItem() == null) return;
        Item item = tree.getContainerDataSource().getItem(event.getItem().getId());
        if (item != null) {
            item.getItemProperty("object").setValue(event.getItem());
            item.getItemProperty("name").setValue(event.getItem().getName());
            item.getItemProperty("content").setValue(event.getItem().getContentMeta());
        }
    }

    @Subscribe
    public void onFilterByTagEvent(FilterByTagEvent event) {
        if (event.getTag() == null) return;
        HierarchicalContainer container = (HierarchicalContainer)tree.getContainerDataSource();
        container.removeAllContainerFilters();
        Tag tag = event.getTag();
        container.addContainerFilter(new FilterByTag("object", tag));
    }

    private void initContainer(HierarchicalContainer container, TItem ti) {
        Item item = addContainerItem(container, ti);
        if (item == null) return;

        if (ti.getKind() == TreeKind.CATEGORY && ti.hasChildren()) {
            ti.getChildren().forEach(child -> initContainer(container, child));
        }
    }

    private Item addContainerItem(HierarchicalContainer container, TItem ti) {
        if (ti == null) return null;
        Item item = container.addItem(ti.getId());
        if (item == null) return null;

        FontAwesome icon = ti.isCategory() ? FontAwesome.FOLDER : FontAwesome.EDIT;

        item.getItemProperty("id").setValue(ti.getId());
        item.getItemProperty("name").setValue(ti.getName());
        item.getItemProperty("icon").setValue(icon);
        item.getItemProperty("kind").setValue(ti.getKind());
        item.getItemProperty("content").setValue(ti.getContentMeta());
        item.getItemProperty("object").setValue(ti);

        if (ti.hasParent()) container.setParent(ti.getId(), ti.getParent().getId());
        return item;
    }

    private void removeItem(HierarchicalContainer container, Object itemId) {
        container.removeItem(itemId);
    }

    private void sortContainer(HierarchicalContainer container) {
        container.sort(new Object[]{"kind", "name"}, new boolean[] {true, true});
    }

    private class FilterByTag implements Container.Filter {

        private final Object propertyId;
        private final Tag filterTag;

        public FilterByTag(Object propertyId, Tag filterTag) {
            this.propertyId = propertyId;
            this.filterTag = filterTag;
        }

        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            if (filterTag == null) return false;
            Property<?> p = item.getItemProperty(propertyId);
            if (p == null) return false;
            TItem ti = (TItem) p.getValue();
            if (ti == null) return false;
            if (ti.isNotice() && ti.getTags().contains(filterTag)) {
                return true;
            }
            return false;
        }

        @Override
        public boolean appliesToProperty(Object propertyId) {
            if ("object".equals(propertyId)) return true;
            return false;
        }
    }
}
