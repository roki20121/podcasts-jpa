package com.roman.podcastplayer.parser;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ChannelParserTest {

    @ParameterizedTest
    @MethodSource("listFullFileNames")
    void parse_newChannel_parsedAllContent(String fileName) throws IOException {
        File podcastFile = getResourceFile(fileName);
        FileInputStream inputStream = new FileInputStream(podcastFile);
        ChannelParser channelParser = new ChannelParser(inputStream);

        channelParser.parse();

        assertTrue(channelParser.hasNewPodcasts());

        Channel channel = channelParser.getChannel();

        assertNotNull(channel);
        assertNotNull(channel.getPodcasts());
        assertNotNull(channel.getLastPublished());
        assertNotNull(channel.getTitle());
        assertNotNull(channel.getDescription());

        for (Podcast podcast : channel.getPodcasts()) {
            assertNotNull(podcast.getUuid());
            assertNotNull(podcast.getPublished());
            assertNotNull(podcast.getTitle());
            assertNotNull(podcast.getAudioUrl());
            assertNotNull(podcast.getDescription());

        }
    }

    @ParameterizedTest
    @MethodSource("getKnownChannelsAndExpectedValues")
    void parse_knownChannel_newContent(String fileName, String lastSavedUuid, int newPodcastsCount)
            throws IOException {
        File channelFile = getResourceFile(fileName);
        FileInputStream inputStream = new FileInputStream(channelFile);
        ChannelParser channelParser = new ChannelParser(inputStream, lastSavedUuid);

        channelParser.parse();

        assertTrue(channelParser.hasNewPodcasts());

        Channel channel = channelParser.getChannel();

        assertNotNull(channel);
        assertEquals(newPodcastsCount, channel.getPodcasts().size());
    }

    @ParameterizedTest
    @MethodSource("getKnownChannel")
    void parse_knownChannel_noNewContent(String fileName, String lastSavedUuid) throws IOException {
        File channelFile = getResourceFile(fileName);
        FileInputStream inputStream = new FileInputStream(channelFile);
        ChannelParser channelParser = new ChannelParser(inputStream, lastSavedUuid);

        channelParser.parse();

        assertFalse(channelParser.hasNewPodcasts());
    }

    @Test
    void parse_emptyStream_throwsException() {
        ChannelParser channelParser = new ChannelParser(new ByteArrayInputStream(new byte[0]));

        assertThrows(IllegalStateException.class,
                channelParser::parse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"channels/cbc_full.xml"})
    void parse_secondCall_throwsException(String fileName) throws IOException {
        File channelFile = getResourceFile(fileName);

        FileInputStream inputStream = new FileInputStream(channelFile);
        ChannelParser channelParser = new ChannelParser(inputStream);

        channelParser.parse();

        assertThrows(IllegalStateException.class, channelParser::parse);
    }

    @Test
    void getChannel_nothingNew_throwsException() {
        ChannelParser channelParser = new ChannelParser(new ByteArrayInputStream(new byte[0]));

        assertThrows(IllegalStateException.class,
                channelParser::getChannel);
    }

    private static Stream<Arguments> getKnownChannel() {
        return Stream.of(
                Arguments.of("channels/cbc_full.xml", "undertheinfluence-8bebd2a3-6654-4e4c-9f60-cfa3d0c516ad"),
                Arguments.of("channels/the_moth_full.xml", "prx_24_9b728a50-99d0-466f-8c07-6621bea4a4fa"),
                Arguments.of("channels/art_of_charm_full.xml", "gid://art19-episode-locator/V0/StxxKuNYrav27a_PSdhKhF7asz3A4zsgvNdgy4VXJWE")
        );
    }

    private static Stream<Arguments> getKnownChannelsAndExpectedValues() {
        return Stream.of(
                Arguments.of("channels/cbc_full.xml", "undertheinfluence-0d4bf888-d020-46eb-bc3d-be04233182dc", 1),
                Arguments.of("channels/cbc_full.xml", "undertheinfluence-b567546b-df58-4e98-b49e-fcc6ca0d9deb", 2),
                Arguments.of("channels/the_moth_full.xml", "prx_24_a67c9505-49be-43c8-aa0e-860e3277ee98", 1),
                Arguments.of("channels/the_moth_full.xml", "prx_24_d7d54a46-bfbc-48ef-9bfd-45c9ef976be1", 2),
                Arguments.of("channels/art_of_charm_full.xml", "gid://art19-episode-locator/V0/fBMA9nY-3IOT5AUT1jgQ7dolw6jhlnDOjvIxYDVPhtU", 1),
                Arguments.of("channels/art_of_charm_full.xml", "gid://art19-episode-locator/V0/s50ISCTCkW6cqBQoZLHu8BQ-RfNK_h5kpzECHmKRFDs", 2)
        );
    }

    private static List<String> listFullFileNames() {
        String[] names = {
                "channels/cbc_full.xml",
                "channels/the_moth_full.xml",
                "channels/art_of_charm_full.xml"
        };

        return Arrays.asList(names);
    }

    private static File getResourceFile(String pathInResources) {
        String path = ChannelParserTest.class.getClassLoader().getResource(pathInResources).getPath();
        return new File(path);
    }

}