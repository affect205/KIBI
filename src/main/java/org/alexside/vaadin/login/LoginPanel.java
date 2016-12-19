package org.alexside.vaadin.login;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.alexside.entity.User;
import org.alexside.enums.UserStatus;
import org.alexside.lang.Strings;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.DataProvider;
import org.alexside.utils.VaadinUtils;
import org.alexside.vaadin.desktop.profile.ProfilePanel;
import org.alexside.vaadin.misc.ActionResponse;
import org.alexside.vaadin.misc.KibiPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 18.10.16.
 */
@SpringComponent
@ViewScope
public class LoginPanel extends KibiPanel {
    private static Logger log = Logger.getLogger(LoginPanel.class.getName());

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private ProfilePanel profilePanel;

    private CustomLayout layout;

    @PostConstruct
    public void onInit() {
        setCaption(Strings.APP_TITLE);
        setSizeFull();
        setContentMargin(true);

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
        submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        submitButton.addStyleName(ValoTheme.BUTTON_SMALL);
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
//        registerButton.setStyleName(BaseTheme.BUTTON_LINK);
//        registerButton.addStyleName("register");
        registerButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        registerButton.addStyleName(ValoTheme.BUTTON_SMALL);
        registerButton.addClickListener(clickEvent -> {
            profilePanel.initData();
            profilePanel.addSaveCallback(user -> {
                if (user != null) {
                    log.info(String.format("[login] login = %s, password = %s", user.getLogin(), user.getPassword()));
                    ActionResponse resp = register(user);
                    if (resp.success()) {
                        UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_DESKTOP);
                        Notification.show(resp.getMessage());
                    } else {
                        Notification.show(resp.getMessage());
                    }
                }
            });
        });
        Button forgotButton = new Button("Забыли пароль?");
        forgotButton.setIcon(FontAwesome.INFO_CIRCLE);
        forgotButton.setWidth("100%");
        forgotButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        forgotButton.addStyleName(ValoTheme.BUTTON_SMALL);
//        forgotButton.setStyleName(BaseTheme.BUTTON_LINK);
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

        setContentAlt(layout);
    }

    @PreDestroy
    public void onDestroy() {
        log.info("[LoginPanel] onDestroy...");
    }

    private ActionResponse login(String login, String password) {
        try {
//            if (!"guest".equals(login)) {
//                login = "alex"; password = "pass";
//            }
            login = "alex22"; password = "password";
            if (dataProvider.getUserCache(login, password) == null) {
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

    private ActionResponse register(User user) {
        Instant now = Instant.now();

        user.setStatus(UserStatus.NEW);
        user.setCreateDate(new Date(now.toEpochMilli()));
        dataProvider.saveUser(user);

        String token = AuthUtils.createJWTToken(user, now.toEpochMilli());
        AuthUtils.UserToken userToken = AuthUtils.parseJWTToken(token);
        String test = "";


//        try {
//            if ("".equals(login.trim())) throw new Exception(Strings.ERROR_WRONG_LOGIN);
//            if ("".equals(password.trim())) throw new Exception(Strings.ERROR_WRONG_PASSWORD);
//            if (dataProvider.getUserCache(login, password) == null) throw new Exception(Strings.ERROR_USER_EXISTS);
//
//            //dataProvider.addUser(login, password);
//            Authentication token = new UsernamePasswordAuthenticationToken(login, password);
//            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
//            SecurityContextHolder.getContext().setAuthentication(token);
//            return ActionResponse.success(Strings.SUCCESS_LOGIN);
//        } catch (Exception e) {
//            return ActionResponse.error(e.getMessage());
//        }
        return null;
    }
}
