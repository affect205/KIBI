package org.alexside.vaadin.misc;

/**
 * Created by abalyshev on 26.10.16.
 */
public class ActionResponse {
    private boolean success;
    private String message;

    private ActionResponse() {}

    public ActionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static ActionResponse success(String message) {
        return new ActionResponse(true, message);
    }

    public static ActionResponse error(String message) {
        return new ActionResponse(false, message);
    }
}
