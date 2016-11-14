package org.alexside.vaadin.desktop.qa;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import org.alexside.entity.Category;
import org.alexside.entity.Notice;
import org.alexside.vaadin.misc.TagItem;

import javax.annotation.PostConstruct;

/**
 * Created by Alex on 14.11.2016.
 */
@SpringComponent
@ViewScope
public class NoticeQAPanel extends Panel {

    @PostConstruct
    public void onInit() {
        setCaption("Записи");
        setSizeFull();

        Category category = new Category("Тест. кат.", null);

        CssLayout wrap = new CssLayout();
        wrap.addStyleName("outlined");
        wrap.addComponents(
                new TagItem(new Notice("Программирование", "...", category)),
                new TagItem(new Notice("С++", "...", category)),
                new TagItem(new Notice("Java", "...", category)),
                new TagItem(new Notice("Vaadin", "...", category)),
                new TagItem(new Notice("NoSQL", "...", category)),
                new TagItem(new Notice("Dishonored2ХОЧУ", "...", category))
        );
        setContent(wrap);
    }
}
