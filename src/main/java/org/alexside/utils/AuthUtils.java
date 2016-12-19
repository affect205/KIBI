package org.alexside.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.jsonwebtoken.*;
import org.alexside.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by abalyshev on 20.10.16.
 */
public class AuthUtils {
    private static Logger log = Logger.getLogger(AuthUtils.class.getName());

    private static final long TOKEN_EXPIRE_MILLIS = TimeUnit.DAYS.toMillis(10);

    private static String key = "MIIEpQIBAAKCAQEA0+9bOlc3kZXFvuPayYIraEDSCwxJjUZvvfkpkD/3LF0/AHL4";

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // TODO: figure out this shit...
        return authentication != null; /*&& authentication.isAuthenticated();*/
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

    public static String createJWTToken(User user, long timestamp) {
        if (user == null) throw new JwtException("Nullable user");
        if (user.getId() == null) throw new JwtException("Nullable userId");

        long now = Instant.now().toEpochMilli();
        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(timestamp))
                .claim("userId", user.getId())
                .claim("login", user.getLogin())
                .claim("password", user.getPassword())
                .claim("email", user.getEmail())
                .signWith(SignatureAlgorithm.HS512, key);

        long expMillis = now + TOKEN_EXPIRE_MILLIS;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public static UserToken parseJWTToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            String userId = claims.get("userId", String.class);
            String login = claims.get("login", String.class);
            String password = claims.get("password", String.class);
            String email = claims.get("email", String.class);
            if (userId != null && login != null && password != null && email != null)
                return new UserToken(userId, login, password, email);
            else
                return null;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return null;
        }
    }

    public static class UserToken {
        public String userId;
        public String login;
        public String password;
        public String email;
        public UserToken(String userId, String login, String password, String email) {
            this.userId = userId;
            this.login = login;
            this.password = password;
            this.email = email;
        }
    }
}
