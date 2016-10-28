package org.alexside.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.alexside.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by abalyshev on 20.10.16.
 */
public class AuthUtils {

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    public static User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            // TODO: redirect to errorView
            return null;
        }
        return new User(auth.getPrincipal().toString(), auth.getCredentials().toString());
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
