package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ChannelManager {

    private EntityManagerFactory factory;

    public ChannelManager(EntityManagerFactory factory) {
        this.factory = factory;
    }


    public void saveChannel(Channel channel, String url) {
        EntityManager manager = factory.createEntityManager();
        channel.setUrl(url);
        manager.persist(channel);
        for (Podcast podcast : channel.getPodcasts()) {
            manager.persist(podcast);
        }

        manager.close();
    }

    public void subscribe(String url, UrlChannelParserConverter converter) throws SQLException, IOException {
        try (ChannelParser parser = converter.openChannelParser(url, null)) {
            parser.parse();
            Channel channel = parser.getChannel();
            saveChannel(channel, url);
        }
    }

    public void unsubscribe(String url) {
        Channel channel = findChannelByUrl(url);

        if (channel == null) {
            return;
        }

        deleteChannel(channel);

    }

    public List<Channel> getAllChannels() {
        EntityManager manager = factory.createEntityManager();

        TypedQuery<Channel> getAllChannels = manager.createNamedQuery("get all Channels", Channel.class);

        List<Channel> resultList = getAllChannels.getResultList();

        manager.close();

        return resultList;
    }

    private void deleteChannel(Channel channel) {
        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();
        manager.remove(channel);
        manager.getTransaction().commit();

        manager.close();
    }

    public Channel findChannelByUrl(String url) {
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Channel> query = builder.createQuery(Channel.class);
        Root<Channel> root = query.from(Channel.class);

        query.where(builder.equal(root.get("url"), url));

        TypedQuery<Channel> typedQuery = manager.createQuery(query);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            manager.close();
        }

    }

}
