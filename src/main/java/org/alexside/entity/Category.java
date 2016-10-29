package org.alexside.entity;


import org.alexside.enums.TreeKind;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Category extends TItem {

    private int sorting;
    private Set<TItem> children;

    public Category(int id) {
        this(id, "");
    }

    public Category(int id, String name) {
        super(id, name, TreeKind.CATEGORY);
        children = new HashSet<>();
    }

    public Category(int id, String name, Category parent, int sorting) {
        super(id, name, TreeKind.CATEGORY, parent);
        this.sorting = sorting;
        this.children = new HashSet<>();
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
    public Set<TItem> getChildren() { return children; }

    public void setChildren(Set<TItem> children) { this.children.addAll(children); }

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
