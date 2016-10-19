package org.alexside.vaadin.login;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.alexside.utils.SpringUtils;
import org.alexside.vaadin.misc.HeaderPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringComponent
@UIScope
public class LoginView extends VerticalLayout implements View {

    @Autowired
    private LoginPanel loginPanel;

    @Autowired
    private HeaderPanel headerPanel;

    public LoginView() {
        setSizeFull();
    }

    @PostConstruct
    public void onInit() {
        headerPanel.setContent(loginPanel);
        addComponents(headerPanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
}
