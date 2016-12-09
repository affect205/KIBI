package org.alexside.entity;

import com.google.gson.Gson;

/**
 * Created by abalyshev on 09.12.16.
 */
public class TItemDump {
    private String id;
    private String name;
    private String kind;
    private String parent;
    private int children;

    public TItemDump(String id, String name, String kind, String parent, int children) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.parent = parent;
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    private static TItemDump toDump(TItem ti) {
        return new TItemDump(ti.getId(), ti.getName(), ti.getKind().toString(), ti.getParent() != null ? ti.getParent().getName() : "none", ti.getChildren().size());
    }

    public static void dump(TItem ti) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(toDump(ti)));
    }
}
