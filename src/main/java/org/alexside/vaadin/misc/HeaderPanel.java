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
        topToolbar.setSizeFull();
        topToolbar.setComponentAlignment(captionLabel, Alignment.TOP_LEFT);
        topToolbar.setExpandRatio(captionLabel, 10);

        contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();

        wrap = new VerticalLayout(topToolbar, contentLayout);
        wrap.setSizeFull();
        wrap.setExpandRatio(contentLayout, 30);
        wrap.setExpandRatio(topToolbar, 1);

        //setContent(wrap);
    }

    @Override
    public void setCaption(String caption) {
        captionLabel.setValue(caption);
    }

    public void addToHeader(Component c) {
        topToolbar.addComponent(c);
        topToolbar.setComponentAlignment(c, Alignment.TOP_RIGHT);
        topToolbar.setExpandRatio(c, 1);
    }

    public void setContentExt(Component c) {
        contentLayout.removeAllComponents();
        contentLayout.addComponent(c);
        setContent(wrap);
    }
}
