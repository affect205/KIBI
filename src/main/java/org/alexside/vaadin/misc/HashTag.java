package org.alexside.vaadin.misc;

import com.vaadin.server.FontAwesome;
import org.alexside.entity.Tag;

/**
 * Created by Alex on 09.12.2016.
 */
public class HashTag extends AbstractTag<Tag> {

    private Tag tag;

    public HashTag(Tag tag) {
        super(tag, FontAwesome.HASHTAG);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
