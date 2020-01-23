package com.roman.podcastplayer.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlChannelParserConverter {

    public ChannelParser openChannelParser(String url, String lastUuid) throws IOException {
        InputStream stream = new URL(url).openConnection().getInputStream();
        return new ChannelParser(stream, lastUuid);
    }

}
