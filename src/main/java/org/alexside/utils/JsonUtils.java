package org.alexside.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by abalyshev on 26.10.16.
 */
public class JsonUtils {

    private static String PATH_USERS_JSON;
    static {
        try {
            PATH_USERS_JSON = AuthUtils.class.getClassLoader().getResource("users.json").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error with users.json path");
        }
    }

    public static JsonObject readUsersJson() throws Exception {
        String usersString = Files.readAllLines(Paths.get(PATH_USERS_JSON)).stream().reduce((s1, s2) -> s1 + s2).orElse("");
        return new Gson().fromJson(usersString, JsonObject.class);
    }

    public static void writeUsersJson(JsonObject usersJson) throws Exception {
        Files.write(Paths.get(PATH_USERS_JSON), usersJson.toString().getBytes("UTF-8"));
    }
}
