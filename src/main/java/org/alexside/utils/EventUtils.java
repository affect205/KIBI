package org.alexside.utils;

import com.google.common.eventbus.EventBus;
import com.vaadin.server.VaadinSession;

/**
 * Created by Alex on 05.11.2016.
 */
public class EventUtils {
    public static void initEventBusInstance() {
        VaadinSession.getCurrent().setAttribute(EventBus.class, new EventBus());
    }

    public static EventBus getEventBusInstance() {
        return VaadinSession.getCurrent().getAttribute(EventBus.class);
    }

    public static void post(Object event) {
        getEventBusInstance().post(event);
    }
}
