package com.roman.podcastplayer.entity;

import java.time.Instant;

import static com.roman.podcastplayer.entity.ChannelUtils.generateRandomString;

public class PodcastUtils {


    /**
     * Generates random podcast without any channel connected to it
     *
     * @return generated new podcast
     */
    public static Podcast generateRandomPodcast(int categories) {
        String uuid = generateRandomString();
        String title = generateRandomString();
        Podcast podcast = new Podcast(uuid, title);
        podcast.setStarred(true);
        podcast.setNewItem(true);
        podcast.setPublished(Instant.now());
        podcast.setAudioUrl(generateRandomString());
        podcast.setDescription(generateRandomString());
        for (int i = 0; i < categories; i++) {
            podcast.addCategory(new Category(generateRandomString()));
        }
        return podcast;
    }

}
