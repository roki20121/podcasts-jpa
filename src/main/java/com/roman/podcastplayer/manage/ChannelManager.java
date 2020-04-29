package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Stateful
public class ChannelManager {

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager manager;

    @Inject
    private UpdatesRetriever retriever;

    public ChannelManager(EntityManager manager) {
        this.manager = manager;
    }

    public ChannelManager() {
    }

    /**
     * Save channel in the database
     *
     * @return channel id in the database
     */

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer saveChannel(Channel channel, String url) {

        channel.setUrl(url);
        manager.persist(channel);
        for (Podcast podcast : channel.getPodcasts()) {
            manager.persist(podcast);
        }

        return channel.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Integer subscribe(String url, UrlChannelParserConverter converter) throws IOException {
        Channel lookupChannel = findChannelByUrl(url);
        if (lookupChannel != null) {
            return lookupChannel.getId();
        }
        try (ChannelParser parser = converter.openChannelParser(url, null)) {
            parser.parse();
            Channel channel = parser.getChannel();
            return saveChannel(channel, url);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void unsubscribe(Integer id) {
        Channel channel = findChannelById(id);
        if (channel == null) {
            return;
        }
        deleteChannel(channel);

    }

    public List<Channel> getAllChannels() {

        TypedQuery<Channel> getAllChannels = manager.createNamedQuery("get all Channels", Channel.class);

        return getAllChannels.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void deleteChannel(Channel channel) {
        manager.remove(channel);
    }

    public Channel findChannelByUrl(String url) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Channel> query = builder.createQuery(Channel.class);
        Root<Channel> root = query.from(Channel.class);

        query.where(builder.equal(root.get("url"), url));
        TypedQuery<Channel> typedQuery = manager.createQuery(query);

        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void starChannel(Integer id, boolean starred) {
        Channel channel = findChannelById(id);

        channel.setStarred(starred);
        manager.persist(channel);
    }

    public Channel findChannelById(Integer id) {

        return manager.find(Channel.class, id);

    }

    public Channel getWithUpdates(Integer id) throws IOException {
        Channel persisted = findChannelById(id);

        String url = persisted.getUrl();
        List<Podcast> podcasts = persisted.getPodcasts();
        String lastUuid = null;

        if (podcasts.size() != 0) {
            lastUuid = podcasts.get(0).getUuid();
        }

        Optional<Channel> updates = retriever.readChannel(url, lastUuid);
        if (!updates.isPresent()) {
            return persisted;
        }

        Channel updatesOnly = updates.get();
        updatesOnly.getPodcasts().forEach(podcast -> podcast.setNewItem(true));

        UpdatesRetriever.mergeChannels(updatesOnly, persisted);
        return updatesOnly;

    }

    public void setRetriever(UpdatesRetriever retriever) {
        this.retriever = retriever;
    }
}
