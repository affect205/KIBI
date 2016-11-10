package org.alexside.vaadin.desktop;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import org.alexside.utils.VaadinUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringView(name = VaadinUtils.VIEW_DESKTOP)
public class DesktopView extends VerticalLayout implements View {

    @Autowired
    private DesktopPanel desktopPanel;

    public DesktopView() {
        setSizeFull();
    }

    @PostConstruct
    public void onInit() {
        addComponents(desktopPanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
}
