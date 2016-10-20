package org.alexside.service;

import org.alexside.entity.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by abalyshev on 19.10.16.
 */
@Service
public class UserService {

    public static List<User> userList;
    static {
        userList = new LinkedList<User>(){{
            add(new User("admin", "admin"));
            add(new User("user", "user"));
        }};

    }

    public User findUser(final String login, final String password) {
        for (User u : userList) {
            if (login.equals(u.getLogin()) && password.equals(u.getPassword())) {
                return u;
            }
        }
        return null;
    }
}
