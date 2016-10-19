package org.alexside.service;

import org.springframework.stereotype.Service;

/**
 * Created by abalyshev on 19.10.16.
 */
@Service
public class LoginService {
    public boolean doLogin(final String login, final String password) {
        return "alex".equals(login) && "pass".equals(password);
    }
}
