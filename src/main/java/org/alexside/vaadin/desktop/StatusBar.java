package org.alexside.vaadin.desktop;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.alexside.events.StatusEvent;
import org.alexside.utils.EventUtils;

import javax.annotation.PostConstruct;

/**
 * Created by Alex on 03.12.2016.
 */
@SpringComponent
@ViewScope
public class StatusBar extends HorizontalLayout {

    private EventBus eventBus;
    private Label statusLabel;

    @PostConstruct
    public void onInit() {
        setSizeFull();
        setHeight("24px");
        setMargin(false);
        setSpacing(true);
        eventBus = EventUtils.getEventBusInstance();
        eventBus.register(this);

        statusLabel = new Label(FontAwesome.COMMENT_O.getHtml(), ContentMode.HTML);
        statusLabel.setSizeFull();
        statusLabel.setHeight("20px");
        statusLabel.addStyleName("status_label");

        addComponents(statusLabel);
        setComponentAlignment(statusLabel, Alignment.TOP_LEFT);
        setExpandRatio(statusLabel, 1);
    }

    @Subscribe
    public void onStatusEvent(StatusEvent event) {
        if (event.getMessage() == null) return;
        statusLabel.setValue(String.format("%s&nbsp;&nbsp;<em>%s</em>", FontAwesome.COMMENT_O.getHtml(), event.getMessage()));
    }
}
