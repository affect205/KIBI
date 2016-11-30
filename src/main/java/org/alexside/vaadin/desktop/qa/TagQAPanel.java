package org.alexside.vaadin.desktop.qa;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.events.TItemQASelectionEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.EventUtils;
import org.alexside.utils.ThemeUtils;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;
import org.alexside.vaadin.misc.TagItem;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Alex on 19.11.2016.
 */
@SpringComponent
@ViewScope
public class TagQAPanel extends KibiPanel {

    private EventBus eventBus;

    private CssLayout wrap;
    private TItem item;

    @PostConstruct
    public void onInit() {
        setCaption("<b>Теги</b>");
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        TextField tagField = new TextField();
        tagField.setSizeFull();
        tagField.setWidth("240px");

        IconButton addButton = IconButton.addTagButton();
        addButton.addClickListener(event -> {
            if (item != null) {
                Tag tag = new Tag(tagField.getValue(), item);
                item.addTag(tag);
                addQATag(tag);
            }
        });

        addToTopToolbar(tagField);
        addToTopToolbar(addButton);

        wrap = new CssLayout();
        wrap.addStyleName(ThemeUtils.LAYOUT_OUTLINED);

        setContentAlt(wrap);
    }

    @PreDestroy
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onTItemSelection(TItemSelectionEvent event) {
        onTItemSelection(event.getItem());
    }

    @Subscribe
    public void onTItemQASelectionEvent(TItemQASelectionEvent event) {
        onTItemSelection(event.getItem());
    }

    private void onTItemSelection(TItem ti) {
        wrap.removeAllComponents();
        this.item = ti;
        if (ti == null) return;
        addQATags(ti);
    }

    private void addQATags(TItem ti) {
        ti.getTags().forEach(tag -> {
            TagItem tagItem = new TagItem(tag);
            wrap.addComponent(tagItem);
        });
    }

    private void addQATag(Tag tag) {
        TagItem tagItem = new TagItem(tag);
        wrap.addComponent(tagItem);
    }
}
