package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
