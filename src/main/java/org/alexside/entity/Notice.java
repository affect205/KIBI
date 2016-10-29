package org.alexside.entity;

import org.alexside.enums.TreeKind;
import org.alexside.utils.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Notice extends TItem {

    private String content;
    private int sorting;

    public Notice(int id, String name, String content, Category parent) {
        super(id, name, TreeKind.NOTICE, parent);
        this.content = content;
    }

    public Notice(int id, String name, String content, Category parent, int sorting) {
        this(id, name, content, parent);
        this.sorting = sorting;
    }

    public String getContent() {
        return content;
    }

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                ", content='" + StringUtils.substring(content, 20) + "...'" +
                ", category=" + (parent != null ? parent.getId() : "null") +
                ", sorting=" + sorting +
                '}';
    }
}
