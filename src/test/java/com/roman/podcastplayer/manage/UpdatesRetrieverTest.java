package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.parser.ChannelParser;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.roman.podcastplayer.entity.ChannelUtils.generateChannel;
import static com.roman.podcastplayer.entity.ChannelUtils.generateRandomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdatesRetrieverTest {


    @Test
    void readChannel_channelReturned() throws IOException {
        UrlChannelParserConverter converter = mock(UrlChannelParserConverter.class);
        ChannelParser parser = mock(ChannelParser.class);
        Channel channel = new Channel();

        when(parser.hasNewPodcasts()).thenReturn(true);
        when(parser.getChannel()).thenReturn(channel);
        when(converter.openChannelParser(Matchers.anyString(), (String) Matchers.isNull())).thenReturn(parser);

        UpdatesRetriever retriever = new UpdatesRetriever(converter);
        Optional<Channel> readChannel = retriever.readChannel("fake url", null);

        assertTrue(readChannel.isPresent());
        assertEquals(channel, readChannel.get());

    }

    @Test
    void merge_newPodcasts_returnMerged() {
        int id = 9;
        int newPodcastsCount = 2;
        int oldPodcastsCount = 10;
        String url = generateRandomString();
        Channel updatesOnly = generateChannel(newPodcastsCount, url);
        Channel oldChannel = generateChannel(oldPodcastsCount, url);
        oldChannel.setId(id);

        UpdatesRetriever.mergeChannels(updatesOnly, oldChannel);
        Channel updatedChannel = updatesOnly;

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
        assertEquals(updatesOnlyPodcasts.get(0), updatedPodcasts.get(0));                                       // first new podcast
        assertEquals(updatesOnlyPodcasts.get(newPodcastsCount - 1), updatedPodcasts.get(newPodcastsCount - 1)); // last new podcast
        assertEquals(oldPodcasts.get(0), updatedPodcasts.get(newPodcastsCount));                                // first old
        assertEquals(oldPodcasts.get(oldPodcastsCount - 1),                                                     // last old
                updatedPodcasts.get(newPodcastsCount + oldPodcastsCount - 1));
    }

}