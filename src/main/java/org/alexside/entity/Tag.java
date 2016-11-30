package org.alexside.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abalyshev on 30.11.16.
 */
@Document(collection = "tags")
public class Tag {
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
}
