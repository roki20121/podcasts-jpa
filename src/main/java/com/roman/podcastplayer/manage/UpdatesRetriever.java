package com.roman.podcastplayer.manage;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.parser.ChannelParser;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class UpdatesRetriever {

    private UrlChannelParserConverter converter;


    public UpdatesRetriever(UrlChannelParserConverter converter) {
        this.converter = converter;
    }

    public UpdatesRetriever() {
    }

    Optional<Channel> readChannel(String url, String lastUuid) throws IOException {

        try (ChannelParser parser = converter.openChannelParser(url, lastUuid)) {
            if (parser.hasNewPodcasts()) {
                return Optional.of(parser.getChannel());
            }
        }

        return Optional.empty();
    }

    /**
     * Merges oldChannel and newChannel into newChannel, copies oldChannel's
     * id, starred and appends old podcasts in chronological order
     */
    static void mergeChannels(Channel newChannel, Channel oldChannel) {
        Objects.requireNonNull(newChannel);
        Objects.requireNonNull(oldChannel);
        if (oldChannel.getId() != null) {
            newChannel.setId(oldChannel.getId());
        }

        newChannel.setStarred(oldChannel.isStarred());
        newChannel.getPodcasts().addAll(oldChannel.getPodcasts());
    }

}
