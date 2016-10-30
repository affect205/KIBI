package org.alexside.vaadin.login;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.BaseTheme;
import org.alexside.lang.Strings;
import org.alexside.service.UserService;
import org.alexside.utils.SpringUtils;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.misc.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 18.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class LoginPanel extends VerticalLayout {

    private static Logger log = Logger.getLogger(LoginPanel.class.getName());

    @Autowired
    private UserService userService;

    private CustomLayout layout;

    public LoginPanel() {
        super();
        setSizeFull();
        setMargin(true);
    }

    @PostConstruct
    public void onInit() {
        log.info("[LoginPanel] onInit...");

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
            String login = loginField.getValue();
            String password = passwordField.getValue();
            log.info(String.format("[LoginPanel] login = %s, password = %s", login, password));
            ActionResponse resp = login(login, password);
            if (resp.success()) {
                UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_DESKTOP);
                Notification.show(resp.getMessage());
            } else {
                Notification.show(resp.getMessage());
            }
        });

        Button registerButton = new Button("Регистрация");
        registerButton.setStyleName(BaseTheme.BUTTON_LINK);
        registerButton.addStyleName("register");
        registerButton.addClickListener(clickEvent -> {
            String login = loginField.getValue();
            String password = passwordField.getValue();
            log.info(String.format("[login] login = %s, password = %s", login, password));
            ActionResponse resp = register(login, password);
            if (resp.success()) {
                UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_DESKTOP);
                Notification.show(resp.getMessage());
            } else {
                Notification.show(resp.getMessage());
            }
        });

        Button forgotButton = new Button("Забыли пароль?");
        forgotButton.setIcon(FontAwesome.INFO_CIRCLE);
        forgotButton.setStyleName(BaseTheme.BUTTON_LINK);
        forgotButton.addStyleName("register");
        forgotButton.addClickListener(clickEvent -> {
            log.info("[forgot] reset password...");
        });


        layout.addComponent(loginField, "login");
        layout.addComponent(passwordField, "password");
        layout.addComponent(submitButton, "submit");
        layout.addComponent(registerButton, "register");
        layout.addComponent(forgotButton, "forgot");
        layout.setSizeFull();

        addComponents(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
    }

    @PreDestroy
    public void onDestroy() {
        log.info("[LoginPanel] onDestroy...");
    }

    private ActionResponse login(String login, String password) {
        try {
            login = "alex";
            password = "pass";
            if (!userService.isExists(login, password)) {
                return ActionResponse.error(Strings.ERROR_USER_NOT_EXISTS);
            }
            Authentication token = new UsernamePasswordAuthenticationToken(login, password);
            SecurityContextHolder.getContext().setAuthentication(token);
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            return ActionResponse.success(Strings.SUCCESS_LOGIN);
        } catch (AuthenticationException e) {
            return ActionResponse.error(e.getMessage());
        }
    }

    private ActionResponse register(String login, String password) {
        try {
            if ("".equals(login.trim())) throw new Exception(Strings.ERROR_WRONG_LOGIN);
            if ("".equals(password.trim())) throw new Exception(Strings.ERROR_WRONG_PASSWORD);
            if (userService.isExists(login, password)) throw new Exception(Strings.ERROR_USER_EXISTS);

            userService.addUser(login, password);
            Authentication token = new UsernamePasswordAuthenticationToken(login, password);
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            return ActionResponse.success(Strings.SUCCESS_LOGIN);
        } catch (Exception e) {
            return ActionResponse.error(e.getMessage());
        }
    }
}
