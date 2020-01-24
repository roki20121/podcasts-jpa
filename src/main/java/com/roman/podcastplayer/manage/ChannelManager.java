package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;

@Stateless
public class ChannelManager {

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManagerFactory factory;

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager manager;

    public ChannelManager(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public ChannelManager() {
    }

    /**
     * Save channel in the database
     *
     * @return channel id in the database
     */
    public Integer saveChannel(Channel channel, String url) {
        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();
        channel.setUrl(url);
        manager.persist(channel);
        for (Podcast podcast : channel.getPodcasts()) {
            manager.persist(podcast);
        }
        manager.getTransaction().commit();

        manager.close();

        return channel.getId();
    }

    public Integer subscribe(String url, UrlChannelParserConverter converter) throws IOException {
        Channel lookupChannel = findChannelByUrl(url);
        if (lookupChannel != null) {
            return null;
        }
        try (ChannelParser parser = converter.openChannelParser(url, null)) {
            parser.parse();
            Channel channel = parser.getChannel();
            return saveChannel(channel, url);
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


    public Channel findChannelById(Integer id) {

        Channel channel = manager.find(Channel.class, id);


        return channel;
    }

}
