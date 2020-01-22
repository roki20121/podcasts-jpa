package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.Objects;

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
        Channel result = typedQuery.getSingleResult();

        manager.close();
        return result;
    }


    /**
     * Merges channel retrieved from parser with oldChannel
     *
     * @param parser     unused object
     * @param oldChannel old channel
     * @return if parser.hasNewPodcasts() returns same object asparser.getChannel(), copies old channel
     * id, starred and appends old podcasts in chronological order. Else returns oldChannel
     */
    public static Channel getUpdatedChannel(ChannelParser parser, Channel oldChannel) throws IOException {

        Objects.requireNonNull(oldChannel);
        parser.parse();

        if (parser.hasNewPodcasts()) {
            Channel newChannel = parser.getChannel();

            if (oldChannel.getId() != null) {
                newChannel.setId(oldChannel.getId());
            }

            newChannel.setStarred(oldChannel.isStarred());
            newChannel.getPodcasts().addAll(oldChannel.getPodcasts());
            return newChannel;
        } else {
            return oldChannel;
        }
    }

}
