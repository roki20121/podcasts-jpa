package com.roman.podcastplayer.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUtils {

    private static String user = "postgres";
    private static String password = "postgres";

    static void executeScript(Path scriptPath) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/podcasts_db";
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement()) {
            String script = String.join("\n", Files.readAllLines(scriptPath));
            statement.execute(script);
        }
    }
}
