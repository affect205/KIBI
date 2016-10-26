package org.alexside.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by abalyshev on 26.10.16.
 */
public class JsonUtils {
    public static synchronized JsonObject getUsers() throws Exception {
        return new Gson().fromJson(getJsonString(), JsonObject.class);
    }

    public static synchronized void addUser(String login, String password) throws Exception {
        String path = AuthUtils.class.getClassLoader().getResource("users.json").getPath();

        JsonObject usersJson = getUsers();

        JsonObject jo = new JsonObject();
        jo.addProperty("login", login);
        jo.addProperty("password", password);
        JsonArray ja = usersJson.getAsJsonArray("users");
        if (ja != null && !ja.contains(jo)) {
            ja.add(jo);
            usersJson.add("users", ja);
        }
        String json = usersJson.toString();
        Files.write(Paths.get(path), usersJson.toString().getBytes("UTF-8"));
    }

    private static String getJsonString() throws Exception {
        List<String> lines = IOUtils.readLines(AuthUtils.class.getClassLoader().getResourceAsStream("users.json"), Charset.forName("UTF-8"));
        return lines.stream().reduce((s1, s2) -> s1 + s2).orElse("{}");
    }
}
