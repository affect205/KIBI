package org.alexside.events;

import org.alexside.entity.TItem;

/**
 * Created by Alex on 11.11.2016.
 */
public class TItemRefreshEvent {
    private TItem item;

    public TItemRefreshEvent(TItem item) {
        this.item = item;
    }

    public TItem getItem() {
        return item;
    }
}
