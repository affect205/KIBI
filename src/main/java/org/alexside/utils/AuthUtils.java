package org.alexside.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vaadin.server.VaadinSession;
import org.alexside.security.AuthManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static boolean isExists(String login, String password) {
        try {
            JsonObject usersJson = JsonUtils.getUsers();
            JsonArray ja = usersJson.getAsJsonArray("users");
            for (JsonElement je : ja) {
                String uLogin = je.getAsJsonObject().get("login").getAsString();
                String uPassword = je.getAsJsonObject().get("password").getAsString();
                System.out.println(String.format("login = %s, password = %s", uLogin, uPassword));
                if (login.equals(uLogin) && password.equals(uPassword)) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
