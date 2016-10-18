package org.alexside.gui;

import com.vaadin.ui.*;

import java.util.logging.Logger;

/**
 * Created by abalyshev on 18.10.16.
 */
public class LoginPanel extends VerticalLayout {

    private Logger log = Logger.getLogger(LoginPanel.class.getName());

    private CustomLayout layout;

    public LoginPanel() {
        super();
        //setHeight("300px");
        //setWidth("300px");
        setSizeFull();
        setMargin(true);

        try {
            layout = new CustomLayout(LoginPanel.class.getClassLoader().getResourceAsStream("templates/auth.html"));
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

        TextField loginField = new TextField();
        loginField.setWidth("160px");
        loginField.setHeight("30px");

        PasswordField passwordField = new PasswordField();
        passwordField.setWidth("160px");
        passwordField.setHeight("30px");

        Button submitButton = new Button("Войти");
        submitButton.addClickListener(clickEvent -> {
            log.info(String.format("login = %s, password = %s", loginField.getValue(), passwordField.getValue()));
        });

        Button registerButton = new Button("Регистрация");
        registerButton.addClickListener(clickEvent -> {
            log.info("do registration");
        });

        layout.addComponent(loginField, "login");
        layout.addComponent(passwordField, "password");
        layout.addComponent(submitButton, "submit");
        layout.addComponent(registerButton, "register");
        layout.setSizeFull();

        addComponents(layout);
        setComponentAlignment(layout, Alignment.TOP_RIGHT);
    }
}
