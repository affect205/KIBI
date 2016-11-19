package org.alexside.vaadin.desktop.qa;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;

import javax.annotation.PostConstruct;

/**
 * Created by Alex on 19.11.2016.
 */
@SpringComponent
@ViewScope
public class TagQAPanel extends Panel {

    @PostConstruct
    public void onInit() {
        setCaption("<b>Теги</b>");
        setSizeFull();

        CssLayout wrap = new CssLayout();
        wrap.addStyleName("outlined");
//        wrap.addComponents(
//                new TagItem(daddy),
//                new TagItem(new Category("С++", null)),
//                new TagItem(new Category("Java", null)),
//                new TagItem(new Category("Vaadin", null)),
//                new TagItem(new Category("NoSQL", null)),
//                new TagItem(new Category("Dishonored2ХОЧУ", null)),
//                new TagItem(new Category("БросайКурить", null))
//        );
        setContent(wrap);
    }
}
