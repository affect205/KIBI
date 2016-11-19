package org.alexside.vaadin.misc;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.alexside.entity.TItem;
import org.alexside.utils.ThemeUtils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Alex on 14.11.2016.
 */
public class TagItem extends VerticalLayout {

    private static final MenuBar.Command TAG_COMMAND =
            (MenuBar.Command) selectedItem -> {
                Notification.show("Action " + selectedItem.getText(), Notification.Type.TRAY_NOTIFICATION);
            };

    private TItem item;
    private Optional<Consumer<TItem>> callback;
    private MenuBar menuBar;

    public TagItem(TItem ti) {
        this.item = ti;
        this.callback = Optional.empty();

        setSizeUndefined();
        addStyleName(ThemeUtils.TAG_ITEM);

        menuBar = new MenuBar();
        menuBar.setHtmlContentAllowed(true);
        menuBar.setAutoOpen(true);

        buildMenuItem(ti, null);
        addComponents(menuBar);
    }

    public void addCallback(Consumer<TItem> callback) {
        this.callback = Optional.ofNullable(callback);
    }

    private void buildMenuItem(TItem ti, MenuBar.MenuItem parent) {
        if (ti == null) return;
        MenuBar.MenuItem mi;
        FontAwesome icon = ti.isNotice() ? FontAwesome.TAG : FontAwesome.FOLDER;
        if (parent == null) {
            MenuBar.Command command = (MenuBar.Command) selectedItem -> {
                callback.ifPresent(c -> c.accept(item));
            };
            mi = menuBar.addItem(String.format("<b>%s</b>", ti.getName()), icon, command);
        } else {
            mi = parent.addItem(String.format("<b>%s</b>", ti.getName()), icon, TAG_COMMAND);
        }

        if (ti.isCategory()) {
            for (TItem child : ti.getChildren()) {
                buildMenuItem(child, mi);
            }
        }
    }
}


