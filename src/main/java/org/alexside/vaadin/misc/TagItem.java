package org.alexside.vaadin.misc;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.utils.ThemeUtils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Alex on 14.11.2016.
 * TODO: отрефакторить в базовый класс
 */
public class TagItem extends VerticalLayout {

    private static final MenuBar.Command TAG_COMMAND =
            (MenuBar.Command) selectedItem -> {
                Notification.show("Action " + selectedItem.getText(), Notification.Type.TRAY_NOTIFICATION);
            };

    private TItem item;
    private Optional<Consumer<TItem>> callback;

    private Tag tag;
    private Optional<Consumer<Tag>> tagCallback;

    private MenuBar menuBar;
    private MenuBar.MenuItem tagMi;

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

    public TagItem(Tag tag) {
        this.tag = tag;
        this.tagCallback = Optional.empty();

//        VerticalLayout layout = new VerticalLayout();
//        setContent(layout);
//
//        ContextMenu contextMenu = new ContextMenu();
//        contextMenu.addItem("Root Item").addItem("Sub item");
//
//        contextMenu.setAsContextMenuOf(layout);

        setSizeUndefined();
        addStyleName(ThemeUtils.TAG_ITEM);

        menuBar = new MenuBar();
        menuBar.setHtmlContentAllowed(true);
        menuBar.setAutoOpen(true);

        MenuBar.Command command = (MenuBar.Command) selectedItem -> {
            tagCallback.ifPresent(c -> c.accept(tag));
        };
        tagMi = menuBar.addItem(String.format("<b>%s</b>", preformat(tag.getName())), FontAwesome.HASHTAG, command);
        tagMi.setDescription(tag.getName());
        addComponents(menuBar);
    }

    public void addCallback(Consumer<TItem> callback) {
        this.callback = Optional.ofNullable(callback);
    }

    public void addTagCallback(Consumer<Tag> callback) {
        this.tagCallback = Optional.ofNullable(callback);
    }

    private void buildMenuItem(TItem ti, MenuBar.MenuItem parent) {
        if (ti == null) return;
        MenuBar.MenuItem mi;
        FontAwesome icon = ti.isNotice() ? FontAwesome.TAG : FontAwesome.FOLDER;
        if (parent == null) {
            MenuBar.Command command = (MenuBar.Command) selectedItem -> {
                callback.ifPresent(c -> c.accept(item));
            };
            tagMi = menuBar.addItem(String.format("<b>%s</b>", preformat(ti.getName())), icon, command);
            tagMi.setDescription(ti.getName());
            mi = tagMi;
        } else {
            mi = parent.addItem(String.format("<b>%s</b>", preformat(ti.getName())), icon, TAG_COMMAND);
        }

        if (ti.isCategory()) {
            for (TItem child : ti.getChildren()) {
                buildMenuItem(child, mi);
            }
        }
    }

    private String preformat(String name) {
        final int maxLen = 21;
        if (name == null) return "";
        if (name.length() > maxLen) {
            String[] split = name.split("\\s");
            if (split.length == 1) return name.substring(0, maxLen) + "...";
            String out = "";
            for (String str : split) {
                if ((out+str).length() > maxLen) break;
                out += str + " ";
            }
            return name.equals(out) ? name : out.trim() + "...";
        }
        return name;
    }

    public void setTagText(String name) {
        if (tagMi != null) tagMi.setText(String.format("<b>%s</b>", preformat(name)));
    }
}


