package org.alexside.vaadin.login;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.alexside.utils.VaadinUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by abalyshev on 19.10.16.
 */
@SpringView(name = VaadinUtils.VIEW_LOGIN)
public class LoginView extends VerticalLayout implements View {

    @Autowired
    private LoginPanel loginPanel;

    public LoginView() {
        setSizeFull();
    }

    @PostConstruct
    public void onInit() {
        addComponents(loginPanel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
}
