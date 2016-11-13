package org.alexside.vaadin.misc;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Action;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.Category;
import org.alexside.entity.Notice;
import org.alexside.entity.TItem;
import org.alexside.enums.TreeKind;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.DataProvider;
import org.alexside.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;

import static org.alexside.utils.ThemeUtils.PANEL_HEADER;

/**
 * Created by abalyshev on 27.10.16.
 */
@SpringComponent
@ViewScope
public class KibiTree extends Panel {

    @Autowired
    protected DataProvider dataProvider;

    protected Label addButton;
    protected Tree tree;
    protected Action addCategory = new Action("Добавить категорию", FontAwesome.FOLDER);
    protected Action addNotice = new Action("Добавить запись", FontAwesome.EDIT);
    protected Action delete = new Action("Удалить", FontAwesome.CLOSE);

    @PostConstruct
    public void onIit() {
        setSizeFull();

        Label captionLabel = new Label("<b>Дерево знаний</b>", ContentMode.HTML);
        captionLabel.setSizeFull();

        addButton = new Label(FontAwesome.PLUS_SQUARE.getHtml(), ContentMode.HTML);
        addButton.setDescription("Добавить категорию");

        HorizontalLayout addWrap = new HorizontalLayout();
        addWrap.addComponent(addButton);
        addWrap.addLayoutClickListener(event -> {
            TItem ti = new Category("Категория_" + Instant.now().toEpochMilli(), null);
            Item added = addContainerItem(
                    (HierarchicalContainer)tree.getContainerDataSource(), ti);
            if (added == null) return;
            tree.expandItem(ti.hashCode());
            tree.setChildrenAllowed(ti.hashCode(), true);
            dataProvider.saveTItem(ti);
        });

        HorizontalLayout topToolbar = new HorizontalLayout(captionLabel, addWrap);
        topToolbar.addStyleName(PANEL_HEADER);
        topToolbar.setSizeFull();
        topToolbar.setComponentAlignment(addWrap, Alignment.TOP_RIGHT);
        topToolbar.setExpandRatio(captionLabel, 1.0f);

        List<TItem> data = dataProvider.getTreeData();
        HierarchicalContainer container = new HierarchicalContainer();
        container.addContainerProperty("name", String.class, "");
        container.addContainerProperty("icon", Resource.class, null);
        container.addContainerProperty("kind", TreeKind.class, TreeKind.UNKNOWN);
        container.addContainerProperty("object", TItem.class, null);
        data.forEach(tItem -> initContainer(container, tItem));
        sortContainer(container);

        tree = new Tree("", container);
        tree.setImmediate(true);
        //tree.addStyleName("tree_item");
        tree.setItemIconPropertyId("icon");
        tree.setItemCaptionPropertyId("name");
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        tree.addValueChangeListener(event -> {
            TItem selected = getSelected();
            if (selected != null) {
                EventUtils.post(new TItemSelectionEvent(selected));
            }
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
                    Item added = addContainerItem(
                            (HierarchicalContainer)tree.getContainerDataSource(), ti);
                    if (added == null) return;
                    tree.expandItem(ti.hashCode());
                    tree.setChildrenAllowed(ti.hashCode(), true);
                } else if (action == addNotice) {
                    TItem ti = new Notice("Запись_" + Instant.now().toEpochMilli(), "", (Category) selected);
                    Item added = addContainerItem(
                            (HierarchicalContainer)tree.getContainerDataSource(), ti);
                    if (added == null) return;
                    tree.setChildrenAllowed(ti.hashCode(), false);
                } else if (action == delete) {
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

        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(topToolbar, tree);
        layout.setExpandRatio(tree, 1.0f);

        setContent(layout);
    }

    public TItem getSelected() {
        Item selected = tree.getItem(tree.getValue());
        if (selected == null) return null;
        return (TItem) selected.getItemProperty("object").getValue();
    }


    private void initContainer(HierarchicalContainer container, TItem ti) {
        Item item = addContainerItem(container, ti);
        if (item == null) return;

        if (ti.getKind() == TreeKind.CATEGORY && ti.hasChildren()) {
            ti.getChildren().forEach(child -> initContainer(container, child));
        }
    }

    private Item addContainerItem(HierarchicalContainer container, TItem ti) {
        Item item = container.addItem(ti.hashCode());
        if (item == null) return null;

        FontAwesome icon = ti.isCategory() ? FontAwesome.FOLDER : FontAwesome.EDIT;

        item.getItemProperty("name").setValue(ti.getName());
        item.getItemProperty("icon").setValue(icon);
        item.getItemProperty("kind").setValue(ti.getKind());
        item.getItemProperty("object").setValue(ti);

        if (ti.hasParent()) container.setParent(ti.hashCode(), ti.getParent().hashCode());
        return item;
    }

    private void removeItem(HierarchicalContainer container, Object itemId) {
        container.removeItem(itemId);
    }

    private void sortContainer(HierarchicalContainer container) {
        container.sort(new Object[]{"kind", "name"}, new boolean[] {true, true});
    }
}
