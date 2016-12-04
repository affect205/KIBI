package org.alexside.vaadin.desktop.qa;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alex on 04.12.2016.
 */
@JavaScript({
        "vaadin://js/tagcloud/tagcloud.js",
        "vaadin://js/tagcloud/tagcloud-connector.js",
        "vaadin://js/tagcloud/jquery-1.11.0.min.js",
//        "vaadin://js/tx3-tag-cloud.css",
        "vaadin://js/tagcloud/jquery.tx3-tag-cloud.js"
})
public class TagCloud extends AbstractJavaScriptComponent {

    ArrayList<ValueChangeListener> listeners = new ArrayList<>();

    public TagCloud() {
        addFunction("onClick", (JavaScriptFunction) arguments -> {
            getState().value = arguments.getString(0);
            for (ValueChangeListener listener: listeners) {
                listener.valueChange();
            }
        });
    }

    public void addValueChangeListener(ValueChangeListener listener) {
        listeners.add(listener);
    }

    public void setValue(String value) {
        getState().value = value;
    }

    public String getValue() {
        return getState().value;
    }

    @Override
    protected TagCloudState getState() {
        return (TagCloudState) super.getState();
    }

    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
}


