package org.alexside.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Alex Balyshev
 * Date: 13.07.16
 */
public enum  TreeKind {
    CATEGORY("C"), NOTICE("N"), UNKNOWN("U");

    TreeKind(String path) {
        this.path = path;
    }

    private String path;

    public String getPath() { return path; }
}
