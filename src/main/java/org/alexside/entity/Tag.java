package org.alexside.entity;

import org.alexside.interfaces.INamed;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abalyshev on 30.11.16.
 */
@Document(collection = "tags")
public class Tag implements INamed {
    @Id
    protected String _id;
    @Field
    protected String name;
    @Transient
    protected List<TItem> referenced;

    public Tag() {}

    public Tag(String id, String name, List<TItem> referenced) {
        this._id = id;
        this.name = name;
        this.referenced = referenced;
    }

    public Tag(String name, TItem ti) {
        this._id = null;
        this.name = name;
        this.referenced = Arrays.asList(ti);
    }

    public Tag(String name) {
        this(null, name, new ArrayList<>());
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TItem> getReferenced() {
        return referenced;
    }

    public void setReference(List<TItem> referenced) {
        this.referenced = referenced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        if (_id != null ? !_id.equals(tag._id) : tag._id != null) return false;
        return getName() != null ? getName().equals(tag.getName()) : tag.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    public static boolean equalsId(Tag tag1, Tag tag2) {
        if (tag1 == null) return tag2 == null;
        if (tag2 == null) return false;
        if (tag1.getId() == null) return tag2.getId() == null;
        return tag1.getId().equals(tag2.getId());
    }
}
