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

    private IconButton() {}

    private IconButton(FontAwesome icon, String descr) {
        addStyleName(HEADER_BUTTON);
        Label label = new Label(icon.getHtml(), ContentMode.HTML);
        label.setDescription(descr);
        addComponent(label);
    }

    public void addClickListener(LayoutEvents.LayoutClickListener event) {
        addLayoutClickListener(event);
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
}
