package org.alexside.entity;


import org.alexside.enums.TreeKind;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
@Document(collection = "titems")
public class Category extends TItem {
    @Field
    private int sorting;
    @Transient
    private List<TItem> children;

    public Category() {
        children = new ArrayList<>();
    }

    public Category(int id) {
        this(id, "", null, -1);
    }

    public Category(int id, String name) {
        this(id, name, null, -1);
    }

    public Category(int id, String name, Category parent) {
        this(id, name, parent, -1);
    }

    public Category(int id, String name, Category parent, int sorting) {
        super(id, name, TreeKind.CATEGORY, parent);
        this.sorting = sorting;
        this.children = new ArrayList<>();
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    @Override
    public boolean hasChildren() { return !children.isEmpty(); }

    @Override
    public List<TItem> getChildren() { return children; }

    public void setChildren(List<TItem> children) { this.children.addAll(children); }

    public void addChild(TItem child) { children.add(child); }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                ", parent=" + (parent != null ? parent.getId() : "null") +
                ", sorting=" + sorting +
                '}';
    }
}
