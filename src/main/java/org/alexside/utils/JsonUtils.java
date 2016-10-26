package org.alexside.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Created by abalyshev on 26.10.16.
 */
public class JsonUtils {

    private static final String USERS_PATH = "C:\\Users\\Alex\\users\\users.json";

    public static synchronized JsonObject getUsers() throws Exception {
        return new Gson().fromJson(getJsonString(), JsonObject.class);
    }

    public static synchronized void addUser(String login, String password) throws Exception {
//        String path = AuthUtils.class.getClassLoader().getResource("users.json").getPath();
//        path = System.getProperty("os.name").contains("indow") ? path.substring(1) : path;

        JsonObject usersJson = getUsers();

        JsonObject jo = new JsonObject();
        jo.addProperty("login", login);
        jo.addProperty("password", password);
        JsonArray ja = usersJson.getAsJsonArray("users");
        if (ja != null && !ja.contains(jo)) {
            ja.add(jo);
            usersJson.add("users", ja);
        }
        Files.write(Paths.get(USERS_PATH), usersJson.toString().getBytes("UTF-8"), TRUNCATE_EXISTING);
    }

    private static String getJsonString() throws Exception {
        //List<String> lines = IOUtils.readLines(AuthUtils.class.getClassLoader().getResourceAsStream("users.json"), Charset.forName("UTF-8"));
        List<String> lines = Files.readAllLines(Paths.get(USERS_PATH), Charset.forName("UTF-8"));
        return lines.stream().reduce((s1, s2) -> s1 + s2).orElse("{}");
    }
}
