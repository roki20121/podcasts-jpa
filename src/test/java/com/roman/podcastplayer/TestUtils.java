package com.roman.podcastplayer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestUtils {

    private static String user = "postgres";
    private static String password = "postgres";
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(getResourceInputStream("test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void executeScript(Path scriptPath) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/podcasts_db";
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement()) {
            String script = String.join("\n", Files.readAllLines(scriptPath));
            statement.execute(script);
        }
    }

    public static InputStream getResourceInputStream(String pathInResources) {
        return TestUtils.class.getClassLoader().getResourceAsStream(pathInResources);
    }

    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

}
