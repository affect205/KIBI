package org.alexside.vaadin.misc;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import static org.alexside.utils.ThemeUtils.PANEL_HEADER;

/**
 * Created by Alex on 19.11.2016.
 */
public class HeaderPanel extends Panel {

    private HorizontalLayout topToolbar;
    private VerticalLayout contentLayout;
    private VerticalLayout wrap;
    private Label captionLabel;

    public HeaderPanel() {
        captionLabel = new Label("", ContentMode.HTML);
        captionLabel.setSizeFull();

        topToolbar = new HorizontalLayout(captionLabel);
        topToolbar.addStyleName(PANEL_HEADER);
        topToolbar.setWidth("100%");
        topToolbar.setHeight("100px");
        topToolbar.setComponentAlignment(captionLabel, Alignment.TOP_LEFT);
        topToolbar.setExpandRatio(captionLabel, 1);

        contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();

        wrap = new VerticalLayout(topToolbar, contentLayout);
        wrap.setSizeFull();
        wrap.setExpandRatio(contentLayout, 1);
        wrap.setComponentAlignment(contentLayout, Alignment.TOP_CENTER);
        wrap.setComponentAlignment(topToolbar, Alignment.TOP_CENTER);

        setContent(wrap);
    }

    @Override
    public void setCaption(String caption) {
        captionLabel.setValue(caption);
    }

    public void addToHeader(Component c, Alignment align, float ratio) {
        topToolbar.addComponent(c);
        topToolbar.setComponentAlignment(c, align);
        topToolbar.setExpandRatio(c, ratio);
    }

    public void addToHeader(Component c, Alignment align) {
        addToHeader(c, align, 1);
    }

    public void addToHeader(Component c) {
        addToHeader(c, Alignment.TOP_RIGHT);
    }

    public void setContentAlt(Component c) {
        contentLayout.removeAllComponents();
        contentLayout.addComponent(c);
        setContent(wrap);
    }

    public void setContentMargin(boolean margin) {
        contentLayout.setMargin(margin);
    }
}
