package org.alexside.vaadin.desktop.qa;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.alexside.entity.TItem;
import org.alexside.entity.Tag;
import org.alexside.events.FilterByTagEvent;
import org.alexside.events.TItemQASelectionEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.DataProvider;
import org.alexside.utils.EventUtils;
import org.alexside.utils.ThemeUtils;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;
import org.alexside.vaadin.misc.TagItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Alex on 19.11.2016.
 */
@SpringComponent
@ViewScope
public class TagQAPanel extends KibiPanel {

    @Autowired
    private DataProvider dataProvider;

    private EventBus eventBus;

    private CssLayout wrap;
    private TItem item;

    @PostConstruct
    public void onInit() {
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        TextField tagField = new TextField();
        tagField.setSizeFull();
        tagField.setWidth("240px");

        IconButton addButton = IconButton.addTagButton();
        addButton.addClickListener(event -> {
            if (item != null) {
                Tag tag = dataProvider.getUniqueTag(new Tag(tagField.getValue(), item));
                item.addTag(tag);
                addQATag(tag);
            }
        });

        final TagCloud tagCloud = new TagCloud();
        tagCloud.setValue("Server-side value");
        tagCloud.addValueChangeListener(
                (TagCloud.ValueChangeListener) () -> Notification.show("Value: " + tagCloud.getValue()));

        VerticalLayout cloudWrap = new VerticalLayout();
        //cloudWrap.setSizeFull();
        cloudWrap.setWidth("960px");
        cloudWrap.setHeight("480px");
        cloudWrap.setMargin(true);
        cloudWrap.addComponents(tagCloud);

        Window cloudWindow = new Window("Облако тегов");
        cloudWindow.setModal(true);
        cloudWindow.setSizeFull();
        cloudWindow.setWidth("960px");
        cloudWindow.setHeight("480px");
        cloudWindow.setContent(cloudWrap);

        IconButton cloudButton = IconButton.cloudButton();
        cloudButton.addClickListener(event -> {
            UI.getCurrent().addWindow(cloudWindow);
        });

        Label captionLabel = new Label("<b>Теги</b>", ContentMode.HTML);
        captionLabel.setSizeFull();

        HorizontalLayout captionWrap = new HorizontalLayout(cloudButton, captionLabel);
        captionWrap.setSpacing(true);

        HorizontalLayout tagWrap = new HorizontalLayout(tagField, addButton);
        tagWrap.setSpacing(true);
        tagWrap.setHeight("60%");

        addToTopToolbar(captionWrap, Alignment.TOP_LEFT);
        addToTopToolbar(tagWrap, Alignment.TOP_RIGHT);

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
        ti.getTags().forEach(this::addQATag);
    }

    private void addQATag(Tag tag) {
        TagItem tagItem = new TagItem(tag);
        tagItem.addTagCallback(t -> {
            EventUtils.post(new FilterByTagEvent(t));
        });
        wrap.addComponent(tagItem);
    }
}
