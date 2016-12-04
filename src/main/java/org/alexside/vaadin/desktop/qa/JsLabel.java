package org.alexside.vaadin.desktop.qa;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@SuppressWarnings("serial")
@JavaScript({"vaadin://js/jslabel-connector.js"})
public class JsLabel extends AbstractJavaScriptComponent {

    public JsLabel(final String xhtml) {
        getState().xhtml = xhtml;
    }

    @Override
    protected JsLabelState getState() {
        return (JsLabelState) super.getState();
    }
}
