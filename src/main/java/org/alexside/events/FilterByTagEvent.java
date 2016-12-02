package org.alexside.events;

import org.alexside.entity.Tag;

/**
 * Created by Alex on 02.12.2016.
 */
public class FilterByTagEvent {
    private Tag tag;
    public FilterByTagEvent(Tag tag) { this.tag = tag; }
    public Tag getTag() { return tag; }
}
