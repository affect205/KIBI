package org.alexside.vaadin.desktop.qa;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import org.alexside.entity.TItem;
import org.alexside.events.TItemQASelectionEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.EventUtils;
import org.alexside.vaadin.misc.TagItem;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Alex on 14.11.2016.
 */
@SpringComponent
@ViewScope
public class NoticeQAPanel extends Panel {

    private static final int LIMIT = 10;

    private Deque<NoticeQA> noticeDeque;
    private EventBus eventBus;

    private CssLayout wrap;

    public NoticeQAPanel() {
        noticeDeque = new ArrayDeque<>(LIMIT);
    }

    @PostConstruct
    public void onInit() {
        setCaption("<b>Записи</b>");
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        wrap = new CssLayout();
        wrap.addStyleName("outlined");

        setContent(wrap);
    }

    @PreDestroy
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onTItemSelection(TItemSelectionEvent event) {
        if (event.getItem() == null) return;
        TItem ti = event.getItem();
        if (ti.isNotice()) addQANotice(ti);
    }

    private void addQANotice(TItem ti) {
        noticeDeque.stream()    // remove duplicate
                .filter(noticeQA -> noticeQA.item.equals(ti))
                .findFirst()
                .ifPresent(this::removeQANotice);
        if (noticeDeque.size() >= LIMIT) removeQANotice();
        TagItem tagItem = new TagItem(ti);
        tagItem.addCallback(tItem ->
                EventUtils.post(new TItemQASelectionEvent(tItem)));
        NoticeQA noticeQA = new NoticeQA(ti, tagItem);
        noticeDeque.push(noticeQA);
        wrap.addComponent(noticeQA.tagItem);
    }

    public void removeQANotice() {
        NoticeQA noticeQA = noticeDeque.removeLast();
        wrap.removeComponent(noticeQA.tagItem);
    }

    public void removeQANotice(NoticeQA noticeQA) {
        wrap.removeComponent(noticeQA.tagItem);
        noticeDeque.remove(noticeQA);
    }

    private class NoticeQA {
        public TItem item;
        public TagItem tagItem;

        public NoticeQA(TItem item, TagItem tagItem) {
            this.item = item;
            this.tagItem = tagItem;
        }
    }
}
