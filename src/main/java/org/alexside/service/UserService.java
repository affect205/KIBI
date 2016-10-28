package org.alexside.service;

import org.alexside.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abalyshev on 19.10.16.
 */
@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public User findUser(final String login, final String password) {
        List<User> users = mongoTemplate.findAll(User.class);
        for (User user : users) {
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public boolean isExists(final String login, final String password) {
        return findUser(login, password) != null;
    }

    public void addUser(final String login, final String password) {
        mongoTemplate.insert(new User(login, password));
    }
}
