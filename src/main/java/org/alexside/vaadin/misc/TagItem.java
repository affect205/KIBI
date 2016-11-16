package org.alexside.vaadin.misc;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.alexside.entity.TItem;
import org.alexside.utils.ThemeUtils;

/**
 * Created by Alex on 14.11.2016.
 */
public class TagItem extends VerticalLayout {

    private TItem item;

    public TagItem(TItem ti) {
        this.item = ti;

        setSizeUndefined();
        setMargin(new MarginInfo(true, true, false, false));
        addStyleName(ThemeUtils.TAG_ITEM);

        Button tagButton = new Button(String.format("<b>#%s</b>", item.getName()));
        tagButton.addStyleName(ValoTheme.BUTTON_TINY);
        tagButton.setCaptionAsHtml(true);
        addComponents(tagButton);
    }
}
