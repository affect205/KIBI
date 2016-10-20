package org.alexside.utils;

import com.vaadin.server.VaadinSession;
import org.alexside.security.AuthManager;

import java.security.Principal;

/**
 * Created by abalyshev on 20.10.16.
 */
public class AuthUtils {

    public static final String USER_WEB_TOKEN = "AUTH_WEB_TOKEN";

    public static void saveUser(AuthManager.AuthInfo info) {
        VaadinSession.getCurrent().setAttribute(Principal.class, info.getUser());
        VaadinSession.getCurrent().setAttribute(USER_WEB_TOKEN, info.getToken());
    }
}
