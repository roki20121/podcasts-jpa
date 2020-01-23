package com.roman.podcastplayer.entity;

import java.time.Instant;
import java.util.UUID;

public class ChannelUtils {

    public static Channel generateChannel(int podcastsCount, String url) {
        Channel channel = new Channel(url, generateRandomString());

        channel.setDescription(channel.getDescription());
        channel.setLastPublished(Instant.now());
        channel.setStarred(true);
        for (int i = 0; i < podcastsCount; i++) {
            Podcast podcast = new Podcast(generateRandomString(), generateRandomString());
            channel.getPodcasts().add(podcast);
        }

        return channel;
    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

}
