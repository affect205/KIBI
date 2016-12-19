package org.alexside.entity;

import org.alexside.enums.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by abalyshev on 20.10.16.
 */
@Document(collection="users")
public class User {
    @Id
    private String _id;
    @Field
    private String login;
    @Field
    private String password;
    @Field
    private String email;
    @Field
    private UserStatus status;
    @Field
    private Date createDate;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getId() { return _id; }

    public void setId(String id) { this._id = id; }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) { this.status = status; }

    public Date getCreateDate() { return createDate; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        return password.equals(user.password);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + _id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static boolean equalsId(User u1, User u2) {
        return equalsId(u1, u2, false);
    }

    public static boolean equalsId(User u1, User u2, boolean nullable) {
        if (!nullable && (u1 == null || u2 == null)) return false;
        if (u1 == null) return u2 == null;
        if (u2 == null) return false;
        if (u1.getId() == null) return u2.getId() == null;
        return u1.getId().equals(u2.getId());
    }
}
