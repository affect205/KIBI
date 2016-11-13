package org.alexside.entity;

import org.alexside.enums.TreeKind;
import org.alexside.utils.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
@Document(collection = "titems")
public class Notice extends TItem {
    @Field
    private String content;
    @Field
    private int sorting;

    public Notice() {}

    public Notice(String name, String content, Category parent, int sorting) {
        this(name, content, parent);
        this.sorting = sorting;
    }

    public Notice(String name, String content, Category parent) {
        super(null, name, TreeKind.NOTICE, parent);
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + _id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                ", content='" + StringUtils.substring(content, 20) + "...'" +
                ", category=" + (parent != null ? parent.getId() : "null") +
                ", sorting=" + sorting +
                '}';
    }
}
