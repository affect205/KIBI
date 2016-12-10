package org.alexside.vaadin.misc;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.alexside.interfaces.INamed;
import org.alexside.utils.ThemeUtils;

import java.util.Optional;
import java.util.function.Consumer;


/**
 * Created by Alex on 09.12.2016.
 */
abstract class AbstractTag<K extends INamed> extends HorizontalLayout {
    private K entity;
    private Optional<Consumer<K>> callback;

    private FontAwesome icon;
    private Label label;
    private IconButton delIcon;
    private Optional<Consumer<K>> delCallback;
    private boolean closeable;

    public AbstractTag(K entity, FontAwesome icon) {
        setSizeUndefined();
        setSpacing(true);
        addStyleName(ThemeUtils.TAG_ITEM);
        setDescription(entity.getName());

        this.entity = entity;
        this.callback = Optional.empty();
        this.delCallback = Optional.empty();
        this.closeable = true;
        this.label = new Label(String.format("%s&nbsp;<b>%s</b>", icon.getHtml(), preformat(entity.getName())), ContentMode.HTML);
        this.label.setSizeFull();
        Label iconLabel = new Label();
        HorizontalLayout tagWrap = new HorizontalLayout(iconLabel, label);
        tagWrap.addStyleName("tag_icon");
        tagWrap.setSizeFull();
        tagWrap.addLayoutClickListener(event -> {
            callback.ifPresent(c -> c.accept(entity));
        });

        this.icon = icon;
        this.delIcon = IconButton.deleteButton();
        this.delIcon.addStyleName(ThemeUtils.TAG_CLOSE);
        delIcon.addClickListener(event -> {
            delCallback.ifPresent(c -> c.accept(entity));
        });

        addComponent(tagWrap);
        if (closeable) addComponent(delIcon);
    }

    public void addCallback(Consumer<K> callback) {
        this.callback = Optional.ofNullable(callback);
    }

    public void addDelCallback(Consumer<K> delCallback) {
        this.delCallback = Optional.ofNullable(delCallback);
    }

    public boolean isCloseable() { return closeable; }

    public void setCloseable(boolean closeable) { this.closeable = closeable; }

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

    public void setText(String name) {
        setDescription(name);
        label.setValue(String.format("%s&nbsp;<b>%s</b>", icon.getHtml(), preformat(name)));
    }
}
