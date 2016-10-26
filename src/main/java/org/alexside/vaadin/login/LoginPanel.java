package org.alexside.vaadin.login;

import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.SpringUtils;
import org.alexside.utils.VaadinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 18.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_PROTOTYPE)
public class LoginPanel extends VerticalLayout {

    private static Logger log = Logger.getLogger(LoginPanel.class.getName());

    @Autowired
    AuthenticationManager authManager;

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
            if (login(login, password)) {
                UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_DESKTOP);
                Notification.show(String.format("С возвращением %s", login));
            } else {
                Notification.show("Ошибка! Неверный логин или пароль.");
            }
        });

        Button registerButton = new Button("Регистрация");
        registerButton.addClickListener(clickEvent -> {
            log.info("do registration");
            String login = loginField.getValue();
            String password = passwordField.getValue();
            if (register(login, password)) {
                UI.getCurrent().getNavigator().navigateTo(VaadinUtils.VIEW_DESKTOP);
                Notification.show(String.format("Регистрация завершена %s", login));
            } else {
                Notification.show("Ошибка! Неверный логин или пароль.");
            }
        });

        layout.addComponent(loginField, "login");
        layout.addComponent(passwordField, "password");
        layout.addComponent(submitButton, "submit");
        layout.addComponent(registerButton, "register");
        layout.setSizeFull();

        addComponents(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
    }

    @PreDestroy
    public void onDestroy() {
        log.info("[LoginPanel] onDestroy...");
    }

    private boolean login(String login, String password) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Authentication token = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login, password));

            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);

            // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
            // used WEBSOCKET_XHR and skipped this step completely.
//            UI.getCurrent().getPushConfiguration().setTransport(Transport.WEBSOCKET_XHR);
//            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    private boolean register(String login, String password) {
        try {
            if (AuthUtils.isExists(login, password)) return false;
            Set<GrantedAuthority> roles = new HashSet<>(Arrays.asList(new SimpleGrantedAuthority("USER")));
            Authentication token = new UsernamePasswordAuthenticationToken(login, password, roles);
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            UI.getCurrent().getPushConfiguration().setTransport(Transport.WEBSOCKET);
//            UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
