package com.roman.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class PodcastTest {

    private static String user = "postgres";
    private static String password = "postgres";

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.roman.podcasts");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    @BeforeAll
    static void createNewDB() throws SQLException, IOException {
        Path scriptPath = Paths.get("scripts/create_all_tables.sql");
        executeScript(scriptPath);
    }

    @AfterAll
    static void dropDB() throws SQLException, IOException {
        Path scriptPath = Paths.get("scripts/drop_all_tables.sql");
        executeScript(scriptPath);
    }

    @Test
    void should_id_be_notnull() {

        Podcast podcast = new Podcast("podcast2", "podcast2");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(podcast);

        transaction.commit();

        assertNotNull(podcast.getId());
    }

    @Test
    void addCategory() {

        Podcast podcast1 = new Podcast("podcast1", "podcast1");
        Category category = new Category("category");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(podcast1);
        entityManager.persist(category);

        podcast1.addCategory(category);
        transaction.commit();

        Podcast podcast11 = entityManager.find(Podcast.class, podcast1.getId());
        assertSame(podcast1, podcast11);

    }

    @Test
    void should_throw_exception_when_uuid_null() {
        assertThrows(NullPointerException.class,
                () -> new Podcast(null, "title"));
    }

    @Test
    void should_throw_exception_when_title_null() {
        assertThrows(NullPointerException.class,
                () -> new Podcast("uuid", null));
    }

    private static void executeScript(Path scriptPath) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/podcasts_db";
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement()) {
            String script = String.join("\n", Files.readAllLines(scriptPath));
            statement.execute(script);
        }
    }
}