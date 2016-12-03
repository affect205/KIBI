package org.alexside.events;

/**
 * Created by Alex on 03.12.2016.
 */
public class StatusEvent {
    private String message;
    public StatusEvent(String message) { this.message = message; }
    public String getMessage() { return message; }
}
