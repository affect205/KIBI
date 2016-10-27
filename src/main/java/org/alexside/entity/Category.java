package org.alexside.entity;


import org.alexside.enums.TreeKind;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 12.07.16
 */
public class Category extends TItem {

    private int sorting;

    public Category(int id) {
        this(id, "");
    }

    public Category(int id, String name) {
        super(id, name, TreeKind.CATEGORY);
    }

    public Category(int id, String name, Category parent, int sorting) {
        super(id, name, TreeKind.CATEGORY, parent);
        this.sorting = sorting;
    }

    public int getSorting() {
        return sorting;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

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
