package org.alexside.vaadin.misc;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import org.alexside.entity.TItem;
import org.alexside.enums.TreeKind;
import org.alexside.utils.DataProvider;
import org.alexside.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by abalyshev on 27.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_SINGLETON)
public class KibiTree extends Panel {

    @Autowired
    protected DataProvider dataProvider;

    protected Tree tree;

    @PostConstruct
    public void onIit() {
        setCaption("Дерево знаний");
        setWidth("300px");

        List<TItem> data = dataProvider.getAllData();
        HierarchicalContainer container = new HierarchicalContainer();
        container.addContainerProperty("id", Integer.class, -1);
        container.addContainerProperty("name", String.class, "");
        container.addContainerProperty("kind", TreeKind.class, TreeKind.UNKNOWN);
        container.addContainerProperty("object", TItem.class, null);
        data.forEach(tItem -> initContainer(container, tItem));

        tree = new Tree("", container);
        //addActionHandler(this);
        tree.setImmediate(true);
        tree.setItemCaptionPropertyId("name");
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        tree.getItemIds().forEach(i -> {
            TreeKind kind = (TreeKind)tree.getItem(i).getItemProperty("kind").getValue();
            tree.setChildrenAllowed(i, false);
            if (kind != null && kind == TreeKind.CATEGORY) {
                tree.setChildrenAllowed(i, true);
                tree.expandItem(i);
            }
        });
        setContent(tree);
    }

    public TItem getSelected() {
        Item selected = tree.getItem(tree.getValue());
        if (selected == null) return null;
        return (TItem) selected.getItemProperty("object").getValue();
    }

    private void initContainer(HierarchicalContainer container, TItem tItem) {
        Item item = container.addItem(tItem.hashCode());
        item.getItemProperty("id").setValue(tItem.getId());
        item.getItemProperty("name").setValue(tItem.getName());
        item.getItemProperty("kind").setValue(tItem.getKind());
        item.getItemProperty("object").setValue(tItem);
        if (tItem.hasParent()) container.setParent(tItem.hashCode(), tItem.getParent().hashCode());
        if (tItem.getKind() == TreeKind.CATEGORY && tItem.hasChildren()) {
            tItem.getChildren().forEach(child -> initContainer(container, child));
        }
    }
}
