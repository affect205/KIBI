package org.alexside.entity;

import org.alexside.enums.TreeKind;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyschev
 * Date: 13.07.16
 */
@Document(collection = "titems")
public abstract class TItem {
    @Id
    protected String _id;
    @Field
    protected String name;
    @Field
    protected TreeKind kind;
    @Reference
    protected TItem parent;

    public TItem() {}

    public TItem(String id, String name, TreeKind kind, TItem parent) {
        this();
        this._id = id;
        this.name = name;
        this.kind = kind;
        this.parent = parent;
    }

    public TItem(String id, String name, TreeKind kind) {
        this(id, name, kind, null);
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
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

    public String getContent() { return ""; }

    public String getContentMeta() {
        return String.format("%s|%s", name, getContent());
    }

    public void setContent(String content) {}

    public boolean isCategory() { return kind == TreeKind.CATEGORY; }

    public boolean isNotice() { return kind == TreeKind.NOTICE; }

    public Set<Tag> getTags() { return new HashSet<>(); }

    public void setTags(Set<Tag> tags) {}

    public void addTag(Tag tag) {}

    @Override
    public String toString() {
        return "TItem{" +
                "id=" + _id +
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
        int result = name.hashCode();
        result = 31 * result + getKind().hashCode();
        if (parent != null) result = 31 * result + getParent().hashCode();
        return result;
    }

    private boolean compareParents(TItem ti) {
        return (parent == null ? 0 : parent.hashCode()) ==
                (ti.getParent() == null ? 0 : ti.getParent().hashCode());
    }
}
