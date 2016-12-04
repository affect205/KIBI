package org.alexside.vaadin.desktop.qa;

import com.vaadin.shared.ui.JavaScriptComponentState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 04.12.2016.
 */
public class TagCloudState extends JavaScriptComponentState {
    public String value;
    public List<TagState> tags;

    public TagCloudState() {
        this.value = "";
        this.tags = new ArrayList<>();
    }
}
