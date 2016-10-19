package org.alexside.vaadin.desktop;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;
import org.alexside.vaadin.misc.HeaderPanel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@UIScope
public class DesktopView extends VerticalLayout implements View {

    @Autowired
    private DesktopPanel desktopPanel;

    @Autowired
    private HeaderPanel headerPanel;

    public DesktopView() {
        setSizeFull();
    }

    @PostConstruct
    public void onInit() {
        headerPanel.setContent(desktopPanel);
        addComponents(headerPanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
}
