package org.alexside.entity;

import org.alexside.enums.TreeKind;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
public abstract class TItem {
    protected int id;
    protected String name;
    protected TreeKind kind;
    protected TItem parent;

    public TItem(int id, String name, TreeKind kind, TItem parent) {
        this(id, name, kind);
        this.parent = parent;
    }

    public TItem(int id, String name, TreeKind kind) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.parent = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeKind getKind() {
        return kind;
    }

    public void setKind(TreeKind kind) {
        this.kind = kind;
    }

    public TItem toTItem() { return (TItem)this; }

    public TItem getParent() {
        return parent;
    }

    public boolean hasParent() { return parent != null; }

    public void setParent(TItem parent) {
        this.parent = parent;
    }

    public boolean hasChildren() { return false; }

    public Set<TItem> getChildren() { return new HashSet<>(); }

    @Override
    public String toString() {
        return "TItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kind=" + kind +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TItem) || o == null) return false;
        TItem tItem = (TItem) o;
        return id == tItem.getId() && kind == tItem.getKind();
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getKind().hashCode();
        return result;
    }
}
