package org.alexside.events;

import org.alexside.entity.TItem;

/**
 * Created by Alex on 19.11.2016.
 */
public class TItemQASelectionEvent {
    private TItem item;

    public TItemQASelectionEvent(TItem item) {
        this.item = item;
    }

    public TItem getItem() {
        return item;
    }

}
