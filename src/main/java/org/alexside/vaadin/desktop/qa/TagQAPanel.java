package org.alexside.vaadin.desktop.qa;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import org.alexside.utils.ThemeUtils;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;

import javax.annotation.PostConstruct;

/**
 * Created by Alex on 19.11.2016.
 */
@SpringComponent
@ViewScope
public class TagQAPanel extends KibiPanel {

    @PostConstruct
    public void onInit() {
        setCaption("<b>Теги</b>");
        setSizeFull();

        IconButton addButton = IconButton.addTagButton();
        addButton.addClickListener(event -> {

        });

        addToTopToolbar(addButton);

        CssLayout wrap = new CssLayout();
        wrap.addStyleName(ThemeUtils.LAYOUT_OUTLINED);

        setContentAlt(wrap);
    }
}
