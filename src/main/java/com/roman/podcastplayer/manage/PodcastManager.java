package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Podcast;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class PodcastManager {

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager manager;

    public PodcastManager(EntityManager manager) {
        this.manager = manager;
    }

    public PodcastManager() {
    }

    public Podcast findPodcastById(Integer id) {
        return manager.find(Podcast.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void savePodcast(Podcast podcast) {
        manager.persist(podcast);
    }

    public void setStarred(Integer podcastId, boolean starred) {
        Podcast podcast = findPodcastById(podcastId);

        podcast.setStarred(starred);

        savePodcast(podcast);
    }

}
