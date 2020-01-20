package com.roman.podcastplayer.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChannelTest {

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
        Channel channel = new Channel("http://channel.example.com", "Test channel");

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(channel);

        transaction.commit();

        assertNotNull(channel.getId());
    }

    @Test
    void addPodcast() {
        Channel channel = new Channel("http://channel.example.com", "Test Channel");
        Podcast podcast = new Podcast("uuid", "Podcast Title");

        channel.addPodcast(podcast);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(channel);
        entityManager.persist(podcast);

        entityManager.detach(channel);
        entityManager.detach(podcast);

        transaction.commit();


        Channel channel1 = entityManager.find(Channel.class, channel.getId());

        assertEquals(1, channel1.getPodcasts().size());
        assertEquals(podcast, channel1.getPodcasts().get(0));

    }
}