package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.mockito.Mockito.*;

class ChannelManagerTest {


    @Test
    void saveChannel_hasPodcastList_allPodcastsSaved() {
        int podcastsCount = 10;
        String url = "http://example.com";
        EntityManager manager = mock(EntityManager.class);

        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        when(factory.createEntityManager()).thenReturn(manager);

        Channel channel = generateChannel(podcastsCount);
        ChannelManager channelManager = new ChannelManager(factory);

        channelManager.saveChannel(channel, url);

        verify(factory, times(1)).createEntityManager();
        verify(manager, times(1)).persist(channel);
        for (Podcast podcast : channel.getPodcasts()) {
            verify(manager, times((1))).persist(podcast);
        }

        verify(manager, times(1)).close();
    }


    private Channel generateChannel(int podcastsCount) {
        Channel channel = new Channel("channel", "title");

        for (int i = 0; i < podcastsCount; i++) {
            Podcast podcast = new Podcast("uuid" + i, "title" + i);
            channel.getPodcasts().add(podcast);
        }

        return channel;
    }
}