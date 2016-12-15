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
import org.alexside.vaadin.misc.HashTag;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;

import static org.alexside.entity.Tag.equalsId;

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

        IconButton addButton = IconButton.addTagButton();
        addButton.addClickListener(event -> {
            saveTag(dataProvider.uniqueTag(new Tag(tagField.getValue(), item)));
        });

        // TODO just for test: remove after
        final TagCloud tagCloud = new TagCloud();
        tagCloud.setValue("Server-side value");

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

//        tagCloud.addValueChangeListener((TagCloud.ValueChangeListener) () -> {
//            Notification.show("Value: " + tagCloud.getValue());
//        });

        tagCloud.addTagClickListener((TagCloud.TagClickListener) (String tagId) -> {
            dataProvider.findTagById(tagId).ifPresent(tag -> {
                Notification.show(String.format("Тег %s добавлен", tag.getName()));
                saveTag(tag);
            });
        });

        IconButton cloudButton = IconButton.cloudButton();
        cloudButton.addClickListener(event -> {
            List<TagState> tags = dataProvider.getRatedTagCache().entrySet().stream()
                    .map(entry -> new TagState(
                            entry.getKey().getId(),
                            entry.getKey().getName(),
                            entry.getValue().intValue()))
                    .collect(Collectors.toList());
            //Collections.shuffle(tags);
            tagCloud.setTags(tags);
            UI.getCurrent().addWindow(cloudWindow);
        });

        Label captionLabel = new Label("<b>Теги</b>", ContentMode.HTML);
        captionLabel.setSizeFull();

        HorizontalLayout captionWrap = new HorizontalLayout(cloudButton, captionLabel);
        captionWrap.setSpacing(true);

        HorizontalLayout tagWrap = new HorizontalLayout(tagField, addButton);
        tagWrap.setSizeFull();
        tagWrap.setSpacing(true);
        tagWrap.setHeight("60%");
        tagWrap.setExpandRatio(tagField, 1);

        addToTopToolbar(captionWrap, Alignment.TOP_LEFT);
        addToTopToolbar(tagWrap, Alignment.TOP_RIGHT);

        wrap = new CssLayout();
        wrap.setSizeFull();
        wrap.addStyleName(ThemeUtils.PANEL_SCROLLABLE);
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
        removeQANotice(tag);
        if (tagDeque.size() >= LIMIT) removeQANotice();
        HashTag hashTag = new HashTag(tag);
        hashTag.addCallback(t -> {
            EventUtils.post(new FilterByTagEvent(t));
        });
        hashTag.addDelCallback(t -> {
            removeQANotice(t);
            if (item != null) {
                item.removeTag(t);
                dataProvider.saveTItem(item);
            }
        });
        tagDeque.push(new TagQA(hashTag, tag));
        wrap.addComponent(hashTag);
    }

    public void removeQANotice() {
        TagQA tagQA = tagDeque.removeLast();
        wrap.removeComponent(tagQA.hashTag);
    }

    public void removeQANotice(Tag tag) {
        Optional<TagQA> tagQA = tagDeque.stream()
                .filter(tqa -> equalsId(tqa.tag, tag))
                .findFirst();
        tagQA.ifPresent(tqa -> {
            wrap.removeComponent(tqa.hashTag);
            tagDeque.remove(tqa);
        });

    }

    private void saveTag(Tag tag) {
        if (item != null) {
            item.addTag(tag);
            dataProvider.saveTItem(item);
            addQATag(tag);
        }
    }

    private static class TagQA {
        public HashTag hashTag;
        public Tag tag;
        public TagQA(HashTag hashTag, Tag tag) {
            this.hashTag = hashTag;
            this.tag = tag;
        }
    }
}
