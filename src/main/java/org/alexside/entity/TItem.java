package org.alexside.entity;

import org.alexside.enums.TreeKind;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
@Document(collection = "titems")
public abstract class TItem {
    public static final String PATH_SEPARATOR = ".";
    @Id
    protected String _id;
    @Field
    protected int id;
    @Field
    protected String name;
    @Field
    protected TreeKind kind;
    @Reference
    protected TItem parent;
    @Field
    protected String path;

    public TItem() {}

    public TItem(int id, String name, TreeKind kind, TItem parent) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.parent = parent;
        this.path = (parent == null ? "" : parent.getPath() + ".") + kind.getPath() + id;
    }

    public TItem(int id, String name, TreeKind kind) {
        this(id, name, kind, null);
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

    public List<TItem> getChildren() { return new ArrayList<>(); }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

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
        TItem ti = (TItem) o;
        return name.hashCode() == ti.getName().hashCode() && kind == ti.getKind() && compareParents(ti);
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getKind().hashCode();
        if (parent != null) result = 31 * result + getParent().hashCode();
        return result;
    }

    private boolean compareParents(TItem ti) {
        return (parent == null ? 0 : parent.hashCode()) ==
                (ti.getParent() == null ? 0 : ti.getParent().hashCode());
    }
}
