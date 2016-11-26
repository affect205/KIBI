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
public class HeaderButton extends HorizontalLayout {

    private HeaderButton() {}

    private HeaderButton(FontAwesome icon, String descr) {
        addStyleName(HEADER_BUTTON);
        Label label = new Label(icon.getHtml(), ContentMode.HTML);
        label.setDescription(descr);
        addComponent(label);
    }

    public void addClickListener(LayoutEvents.LayoutClickListener event) {
        addLayoutClickListener(event);
    }

    public static HeaderButton addButton() {
        return new HeaderButton(FontAwesome.PLUS_SQUARE, "Добавить категорию");
    }

    public static HeaderButton codeButton() {
        return new HeaderButton(FontAwesome.CODE, "Редактирование");
    }

    public static HeaderButton eyeButton() {
        return new HeaderButton(FontAwesome.EYE, "Просмотр");
    }

    public static HeaderButton urlButton() {
        return new HeaderButton(FontAwesome.SHARE, "Загрузить из интернета");
    }
}
