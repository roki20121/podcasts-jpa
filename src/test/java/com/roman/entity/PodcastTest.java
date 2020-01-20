package com.roman.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PodcastTest {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.roman.podcasts");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();

    @BeforeAll
    static void createNewDB() throws SQLException, IOException {
        Path scriptPath = Paths.get("scripts/create_all_tables.sql");
        TestUtils.executeScript(scriptPath);
    }

    @AfterAll
    static void dropDB() throws SQLException, IOException {
        entityManager.close();
        entityManagerFactory.close();
        Path scriptPath = Paths.get("scripts/drop_all_tables.sql");
        TestUtils.executeScript(scriptPath);
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

        String categoryName = "category";
        Podcast podcast1 = new Podcast("podcast1", "podcast1");
        Category category = new Category(categoryName);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(podcast1);
        entityManager.persist(category);

        podcast1.addCategory(category);
        transaction.commit();

        Podcast podcast11 = entityManager.find(Podcast.class, podcast1.getId());
        assertSame(podcast1, podcast11);

        assertEquals(categoryName, podcast11.getCategories().iterator().next().getName());

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

    @Test
    void should_persist_timestamp_when_notnull() {
        Podcast podcast = new Podcast("uuid", "Podcast");
        Instant published = Instant.now();
        podcast.setPublished(published);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(podcast);
        entityManager.detach(podcast);

        transaction.commit();

        Podcast podcast1 = entityManager.find(Podcast.class, podcast.getId());

        assertNotSame(podcast, podcast1);
        assertEquals(published, podcast1.getPublished());
    }

    @Test
    void should_throw_when_uuid_exist() {
        Podcast podcast1 = new Podcast("uuid", "Podcast 1");
        Podcast podcast2 = new Podcast("uuid", "Podcast 2");

        assertEquals(podcast1, podcast2);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(podcast1);
            entityManager.persist(podcast2);
        });


        transaction.commit();
    }

}