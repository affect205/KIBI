package org.alexside.vaadin.misc;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import static org.alexside.utils.ThemeUtils.PANEL_FOOTER;
import static org.alexside.utils.ThemeUtils.PANEL_HEADER;

/**
 * Created by Alex on 19.11.2016.
 */
public class KibiPanel extends Panel {

    private HorizontalLayout topToolbar;
    private HorizontalLayout bottomToolbar;
    private VerticalLayout contentLayout;
    private VerticalLayout wrap;
    private Label captionLabel;

    public KibiPanel() {
        captionLabel = new Label("", ContentMode.HTML);
        captionLabel.setSizeFull();
        captionLabel.setVisible(false);

        topToolbar = new HorizontalLayout(captionLabel);
        topToolbar.addStyleName(PANEL_HEADER);
        topToolbar.setWidth("100%");
        topToolbar.setHeight("100px");
        topToolbar.setComponentAlignment(captionLabel, Alignment.TOP_LEFT);
        topToolbar.setExpandRatio(captionLabel, 1);

        contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();

        bottomToolbar = new HorizontalLayout();
        bottomToolbar.addStyleName(PANEL_HEADER);
        bottomToolbar.addStyleName(PANEL_FOOTER);
        bottomToolbar.setWidth("100%");
        bottomToolbar.setHeight("100px");
        bottomToolbar.setVisible(false);

        wrap = new VerticalLayout(topToolbar, contentLayout, bottomToolbar);
        wrap.setSizeFull();
        wrap.setExpandRatio(contentLayout, 1);
        wrap.setComponentAlignment(topToolbar, Alignment.TOP_CENTER);
        wrap.setComponentAlignment(contentLayout, Alignment.TOP_CENTER);
        wrap.setComponentAlignment(bottomToolbar, Alignment.BOTTOM_CENTER);

        setContent(wrap);
    }

    @Override
    public void setCaption(String caption) {
        captionLabel.setValue(caption);
        captionLabel.setVisible(true);
    }

    public void setContentAlt(Component c) {
        contentLayout.removeAllComponents();
        contentLayout.addComponent(c);
        setContent(wrap);
    }

    private void addToToolbar(ToolbarType type, Component c, Alignment align, float ratio) {
        if (type == ToolbarType.TOP) {
            topToolbar.addComponent(c);
            topToolbar.setComponentAlignment(c, align);
            topToolbar.setExpandRatio(c, ratio);
        } else if (type == ToolbarType.BOTTOM) {
            bottomToolbar.addComponent(c);
            bottomToolbar.setComponentAlignment(c, align);
            bottomToolbar.setExpandRatio(c, ratio);
        }
    }

    public void addToTopToolbar(Component c, Alignment align, float ratio) {
        addToToolbar(ToolbarType.TOP, c, align, ratio);
    }

    public void addToTopToolbar(Component c, Alignment align) {
        addToTopToolbar(c, align, 1);
    }

    public void addToTopToolbar(Component c) {
        addToTopToolbar(c, Alignment.TOP_RIGHT, 1);
    }

    public void addToBottomToolbar(Component c, Alignment align, float ratio) {
        addToToolbar(ToolbarType.BOTTOM, c, align, ratio);
    }

    public void addToBottomToolbar(Component c, Alignment align) {
        addToBottomToolbar(c, align, 1);
    }

    public void addToBottomToolbar(Component c) {
        addToBottomToolbar(c, Alignment.BOTTOM_LEFT, 1);
    }

    public void setContentMargin(boolean margin) {
        contentLayout.setMargin(margin);
    }

    public void showBottomToolbar(boolean show) {
        bottomToolbar.setVisible(show);
    }

    private enum  ToolbarType {
        TOP, BOTTOM
    }
}
