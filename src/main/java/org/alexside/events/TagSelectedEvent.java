package org.alexside.events;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 11.12.2016.
 */
public class TagSelectedEvent {
    private List<String> selected;
    public TagSelectedEvent(String selected) { this.selected = Arrays.asList("#" + selected); }
    public TagSelectedEvent(List<String> selected) { this.selected = selected; }
    public String getSelected() { return selected.stream().collect(Collectors.joining(",","#","")); }
}
