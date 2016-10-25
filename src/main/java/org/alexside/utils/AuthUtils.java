package org.alexside.utils;

import com.vaadin.server.VaadinSession;
import org.alexside.security.AuthManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    public static Authentication getUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isExists(AuthenticationManager authManager, String login, String password) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(login, password)) != null;
    }
}
