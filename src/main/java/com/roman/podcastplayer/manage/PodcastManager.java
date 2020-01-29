package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Category;
import com.roman.podcastplayer.entity.Podcast;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class PodcastManager {

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager manager;

    @Inject
    private CategoryManager categoryManager;

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

    public void addCategory(Integer podcastId, Integer categoryId) {
        Podcast podcast = findPodcastById(podcastId);
        Category category = categoryManager.findCategoryById(categoryId);
        podcast.addCategory(category);

        savePodcast(podcast);
    }

    public void removeCategory(Integer podcastId, Integer categoryId) {
        Podcast podcast = findPodcastById(podcastId);
        Category category = categoryManager.findCategoryById(categoryId);

        podcast.getCategories().remove(category);

        savePodcast(podcast);
    }

}
