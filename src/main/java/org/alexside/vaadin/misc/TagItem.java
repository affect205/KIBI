package org.alexside.vaadin.misc;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.alexside.entity.TItem;

/**
 * Created by Alex on 14.11.2016.
 */
public class TagItem extends VerticalLayout {

    private TItem item;

    public TagItem(TItem ti) {
        this.item = ti;

        setSizeUndefined();
        setMargin(new MarginInfo(false, true, false, false));

        Button tagButton = new Button(String.format("<b>#%s</b>", item.getName()));
        tagButton.setCaptionAsHtml(true);
        addComponents(tagButton);
    }
}
