package org.alexside.vaadin.misc;

import com.vaadin.server.FontAwesome;
import org.alexside.entity.TItem;

/**
 * Created by Alex on 10.12.2016.
 */
public class QATag extends AbstractTag<TItem> {
    private TItem item;

    public QATag(TItem item) {
        super(item, FontAwesome.TAG);
        this.item = item;
    }

    public TItem getItem() {
        return item;
    }

    public void setItem(TItem item) {
        this.item = item;
    }
}
