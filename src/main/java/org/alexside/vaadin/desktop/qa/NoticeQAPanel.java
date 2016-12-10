package org.alexside.vaadin.desktop.qa;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.alexside.entity.TItem;
import org.alexside.events.TItemQASelectionEvent;
import org.alexside.events.TItemRefreshEvent;
import org.alexside.events.TItemSelectionEvent;
import org.alexside.utils.EventUtils;
import org.alexside.utils.ThemeUtils;
import org.alexside.vaadin.misc.IconButton;
import org.alexside.vaadin.misc.KibiPanel;
import org.alexside.vaadin.misc.QATag;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayDeque;
import java.util.Deque;

import static org.alexside.entity.TItem.equalsId;

/**
 * Created by Alex on 14.11.2016.
 */
@SpringComponent
@ViewScope
public class NoticeQAPanel extends KibiPanel {

    private static final int LIMIT = 10;

    private Deque<NoticeQA> noticeDeque;
    private EventBus eventBus;

    private CssLayout wrap;

    public NoticeQAPanel() {
        noticeDeque = new ArrayDeque<>(LIMIT);
    }

    @PostConstruct
    public void onInit() {
        setSizeFull();

        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        wrap = new CssLayout();
        wrap.setSizeFull();
        wrap.addStyleName(ThemeUtils.PANEL_SCROLLABLE);
        wrap.addStyleName(ThemeUtils.LAYOUT_OUTLINED);

        Label captionLabel = new Label("<b>Записи</b>", ContentMode.HTML);
        IconButton noticeButton = IconButton.noticeButton();

        HorizontalLayout captionWrap = new HorizontalLayout(noticeButton, captionLabel);
        captionWrap.setSpacing(true);

        addToTopToolbar(captionWrap, Alignment.TOP_LEFT);

        setContentAlt(wrap);
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

    @Subscribe
    public void onTItemRefreshEvent(TItemRefreshEvent event) {
        if (event.getItem() == null) return;
        TItem ti = event.getItem();
        noticeDeque.forEach(noticeQA -> {
            if (noticeQA.item.equals(ti)) {
                noticeQA.tagItem.setText(ti.getName());
            }
        });
    }

    private void addQANotice(TItem ti) {
        removeQANotice(ti);
        if (noticeDeque.size() >= LIMIT) removeQANotice();
        QATag tagItem = new QATag(ti);
        tagItem.addCallback(tItem ->
                EventUtils.post(new TItemQASelectionEvent(tItem)));
        tagItem.addDelCallback(this::removeQANotice);
        NoticeQA noticeQA = new NoticeQA(ti, tagItem);
        noticeDeque.push(noticeQA);
        wrap.addComponent(noticeQA.tagItem);
    }

    public void removeQANotice() {
        NoticeQA noticeQA = noticeDeque.removeLast();
        wrap.removeComponent(noticeQA.tagItem);
    }

    public void removeQANotice(TItem ti) {
        noticeDeque.stream()
                .filter(noticeQA -> equalsId(noticeQA.item, ti))
                .findFirst()
                .ifPresent(noticeQA -> {
                    wrap.removeComponent(noticeQA.tagItem);
                    noticeDeque.remove(noticeQA);
                });
    }

    private class NoticeQA {
        public TItem item;
        public QATag tagItem;

        public NoticeQA(TItem item, QATag tagItem) {
            this.item = item;
            this.tagItem = tagItem;
        }
    }
}
