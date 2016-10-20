package org.alexside.security;

import com.vaadin.spring.annotation.SpringComponent;
import org.alexside.entity.User;
import org.alexside.service.UserService;
import org.alexside.utils.AuthUtils;
import org.alexside.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.security.Principal;

/**
 * Created by abalyshev on 20.10.16.
 */
@SpringComponent
@Scope(SpringUtils.SCOPE_SINGLETON)
public class AuthManager {

    @Autowired
    UserService userService;

    public static class AuthInfo {
        private Principal user;
        private String token;

        public AuthInfo(Principal user, String token) {
            this.user = user;
            this.token = token;
        }

        public Principal getUser() { return user; }
        public String getToken() { return token; }
    }

    public AuthInfo authenticate(String login, String password) {
        User user = userService.findUser(login, password);
        if (user == null) return null;
        AuthInfo authInfo = new AuthInfo(user, String.valueOf(user.hashCode()));
        return authInfo;
    }

    public boolean doLogin(String login, String password) {
        AuthInfo authInfo = authenticate(login, password);
        AuthUtils.saveUser(authInfo);
        return authInfo != null;
    }
}
