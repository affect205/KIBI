package org.alexside.vaadin.misc;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import static org.alexside.utils.ThemeUtils.HEADER_BUTTON;

/**
 * Created by Alex on 15.11.2016.
 */
public class IconButton extends HorizontalLayout {

    private Label label;
    private FontAwesome icon;

    private IconButton() {}

    private IconButton(FontAwesome icon, String descr) {
        addStyleName(HEADER_BUTTON);
        this.icon = icon;
        this.label = new Label(icon.getHtml(), ContentMode.HTML);
        this.label.setDescription(descr);
        addComponent(label);
    }

    private IconButton(FontAwesome icon, String text, String descr) {
        addStyleName(HEADER_BUTTON);
        this.icon = icon;
        this.label = new Label(String.format("<span>%s&nbsp;%s</span>", icon.getHtml(), text), ContentMode.HTML);
        if (descr != null && !descr.isEmpty()) label.setDescription(descr);
        addComponent(label);
    }

    public void setText(String text) {
        label.setValue(String.format("<span>%s&nbsp;%s</span>", icon.getHtml(), text));
    }

    public void addClickListener(LayoutEvents.LayoutClickListener event) {
        addLayoutClickListener(event);
    }

    public static IconButton addTagButton() {
        return new IconButton(FontAwesome.PLUS_SQUARE, "Добавить тег");
    }

    public static IconButton addButton() {
        return new IconButton(FontAwesome.PLUS_SQUARE, "Добавить категорию");
    }

    public static IconButton codeButton() {
        return new IconButton(FontAwesome.CODE, "Редактирование");
    }

    public static IconButton eyeButton() {
        return new IconButton(FontAwesome.EYE, "Просмотр");
    }

    public static IconButton urlButton() {
        return new IconButton(FontAwesome.SHARE, "Загрузить из интернета");
    }

    public static IconButton searchButton() {
        return new IconButton(FontAwesome.SEARCH, "");
    }

    public static IconButton userButton(String text) {
        return new IconButton(FontAwesome.USER, text, "");
    }

    public static IconButton cloudButton() { return new IconButton(FontAwesome.CLOUD, "Облако тегов"); }

    public static IconButton noticeButton() { return new IconButton(FontAwesome.EDIT, ""); }

    public static IconButton clearButton() { return new IconButton(FontAwesome.CLOSE, "Очистить"); }

    public static IconButton deleteButton() { return new IconButton(FontAwesome.CLOSE, "Удалить"); }
}
