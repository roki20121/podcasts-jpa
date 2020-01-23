package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.ChannelUtils;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChannelManagerTest {


    @Test
    void saveChannel_hasPodcastList_allPodcastsSaved() {
        int podcastsCount = 10;
        String url = "http://example.com";
        EntityManager manager = mock(EntityManager.class);

        EntityManagerFactory factory = mock(EntityManagerFactory.class);
        when(factory.createEntityManager()).thenReturn(manager);

        Channel channel = ChannelUtils.generateChannel(podcastsCount, url);
        ChannelManager channelManager = new ChannelManager(factory);

        channelManager.saveChannel(channel, url);

        verify(factory, times(1)).createEntityManager();
        verify(manager, times(1)).persist(channel);
        for (Podcast podcast : channel.getPodcasts()) {
            verify(manager, times((1))).persist(podcast);
        }

        verify(manager, times(1)).close();
    }


    @Test
    void getUpdatedChannel_nothingNew_returnsOldChannel() throws IOException {

        ChannelParser parser = mock(ChannelParser.class);
        when(parser.hasNewPodcasts()).thenReturn(false);

        Channel channel = new Channel();

        Channel updatedChannel = ChannelManager.getUpdatedChannel(parser, channel);

        assertSame(channel, updatedChannel);
    }

    @Test
    void getUpdatedChannel_oldChannelNull_throwsException() {
        ChannelParser parser = mock(ChannelParser.class);

        assertThrows(NullPointerException.class,
                () -> ChannelManager.getUpdatedChannel(parser, null));
    }

    @Test
    void getUpdatedChannel_newPodcasts_returnMerged() throws IOException {
        int id = 9;
        int newPodcastsCount = 2;
        int oldPodcastsCount = 10;
        String url = generateRandomString();
        ChannelParser parser = mock(ChannelParser.class);
        when(parser.hasNewPodcasts()).thenReturn(true);


        Channel updatesOnly = generateChannel(newPodcastsCount, url);
        Channel oldChannel = generateChannel(oldPodcastsCount, url);
        oldChannel.setId(id);

        when(parser.getChannel()).thenReturn(updatesOnly);

        Channel updatedChannel = ChannelManager.getUpdatedChannel(parser, oldChannel);


        assertSame(updatesOnly, updatedChannel);
        assertEquals(id, updatedChannel.getId());
        assertEquals(url, updatedChannel.getUrl());
        assertEquals(updatesOnly.getDescription(), updatedChannel.getDescription());
        assertEquals(updatesOnly.getLastPublished(), updatedChannel.getLastPublished());
        assertEquals(updatedChannel.isStarred(), oldChannel.isStarred());

        List<Podcast> updatedPodcasts = updatedChannel.getPodcasts();
        List<Podcast> updatesOnlyPodcasts = updatesOnly.getPodcasts();
        List<Podcast> oldPodcasts = oldChannel.getPodcasts();

        assertEquals(updatedPodcasts.size(), newPodcastsCount + oldPodcastsCount);
        assertEquals(updatesOnlyPodcasts.get(0), updatedPodcasts.get(0));                                    // first new podcast
        assertEquals(updatesOnlyPodcasts.get(newPodcastsCount - 1), updatedPodcasts.get(newPodcastsCount - 1));  // last new podcast
        assertEquals(oldPodcasts.get(0), updatedPodcasts.get(newPodcastsCount));                             // first old
        assertEquals(oldPodcasts.get(oldPodcastsCount - 1),
                updatedPodcasts.get(newPodcastsCount + oldPodcastsCount - 1));                                  // last old


    }

    private static Channel generateChannel(int podcastsCount, String url) {
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

    private static String generateRandomString() {
        return UUID.randomUUID().toString();
    }
}