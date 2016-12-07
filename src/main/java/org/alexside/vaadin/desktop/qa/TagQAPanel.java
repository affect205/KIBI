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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 19.11.2016.
 */
@SpringComponent
@ViewScope
public class TagQAPanel extends KibiPanel {

    private static final int LIMIT = 10;

    @Autowired
    private DataProvider dataProvider;

    private EventBus eventBus;

    private Deque<TagQA> tagDeque;

    private CssLayout wrap;
    private TItem item;

    @PostConstruct
    public void onInit() {
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        tagDeque = new ArrayDeque<>();

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
        List<TagState> tags = dataProvider.getTagCache().stream()
                .map(t -> new TagState(t.getId(), t.getName(), 30))
                .collect(Collectors.toList());
        tagCloud.setTags(tags);

        VerticalLayout cloudWrap = new VerticalLayout();
        cloudWrap.setWidth("960px");
        cloudWrap.setHeight("480px");
        cloudWrap.setMargin(true);
        cloudWrap.addComponents(tagCloud);

        Window cloudWindow = new Window("Облако тегов");
        cloudWindow.setModal(true);
        cloudWindow.setWidth("960px");
        cloudWindow.setHeight("480px");
        cloudWindow.setContent(cloudWrap);

        tagCloud.addValueChangeListener((TagCloud.ValueChangeListener) () -> {
            Notification.show("Value: " + tagCloud.getValue());
        });

        tagCloud.addTagClickListener((TagCloud.TagClickListener) (String tagId) -> {
            Notification.show(String.format("Tag id: %s", tagId));
            dataProvider.findTagById(tagId).ifPresent(this::addQATag);
            cloudWindow.close();
        });

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
        tagDeque.stream()
                .filter(noticeQA -> noticeQA.tag.equals(tag))
                .findFirst()
                .ifPresent(this::removeQANotice);
        if (tagDeque.size() >= LIMIT) removeQANotice();
        TagItem tagItem = new TagItem(tag);
        tagItem.addTagCallback(t -> {
            EventUtils.post(new FilterByTagEvent(t));
        });
        tagDeque.push(new TagQA(tagItem, tag));
        wrap.addComponent(tagItem);
    }

    public void removeQANotice() {
        TagQA tagQA = tagDeque.removeLast();
        wrap.removeComponent(tagQA.tagItem);
    }

    public void removeQANotice(TagQA tagQA) {
        wrap.removeComponent(tagQA.tagItem);
        tagDeque.remove(tagQA);
    }

    private static class TagQA {
        public TagItem tagItem;
        public Tag tag;
        public TagQA(TagItem tagItem, Tag tag) {
            this.tagItem = tagItem;
            this.tag = tag;
        }
    }
}
