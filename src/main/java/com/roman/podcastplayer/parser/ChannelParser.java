package com.roman.podcastplayer.parser;


import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChannelParser {

    private static final SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    private InputStream inputStream;
    private String lastStoredGuid;

    private List<Podcast> podcasts = new ArrayList<>();

    private boolean hasNewPodcasts;
    private Channel channel;

    public ChannelParser(InputStream inputStream) {
        this(inputStream, null);
    }

    public ChannelParser(InputStream inputStream, String lastStoredGuid) {
        this.inputStream = inputStream;
        this.lastStoredGuid = lastStoredGuid;
    }

    public void parse() throws IOException {
        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);
            Channel channel = null;
            Podcast podcast = null;
            boolean inPodcast = false;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        switch (name) {
                            case "channel":
                                channel = new Channel();
                                inPodcast = false;
                                break;
                            case "item":
                                podcast = new Podcast();
                                inPodcast = true;
                                break;
                            case "title":
                                String title = parser.nextText().trim();
                                if (inPodcast) {
                                    podcast.setTitle(title);
                                } else {
                                    channel.setTitle(title);
                                }
                                break;
                            case "description":
                                String desc = parser.nextText().trim();
                                if (inPodcast) {
                                    podcast.setDescription(desc);
                                } else {
                                    channel.setDescription(desc);
                                }
                                break;
                            case "enclosure":
                                if (inPodcast) {
                                    String url = parser.getAttributeValue(0);
                                    String type = parser.getAttributeValue(null, "type");
                                    if (type != null) {
                                        podcast.setAudioUrl(url);
                                    }
                                }
                                break;
                            case "pubDate":
                                String pubDate = parser.nextText();
                                Instant instant = getPublishInstant(pubDate);
                                if (inPodcast) {
                                    podcast.setPublished(instant);
                                } else {
                                    channel.setLastPublished(instant);
                                }
                                break;
                            case "guid":
                                if (inPodcast) {
                                    String guid = parser.nextText().trim();
                                    podcast.setUuid(guid);
                                }
                                break;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equals("channel")) {
                            this.channel = channel;
                        }
                        if (name.equals("item")) {
                            if (podcast.getUuid().equals(lastStoredGuid)) {
                                this.channel = channel;
                                return;
                            }
                            hasNewPodcasts = true;
                            podcasts.add(podcast);
                        }
                }
                eventType = parser.next();
            }

            this.channel = channel;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNewPodcasts() {
        return hasNewPodcasts;
    }

    public Channel getChannel() {
        if (!hasNewPodcasts)
            throw new IllegalStateException("Nothing new!");
        channel.setPodcasts(podcasts);
        if (channel.getLastPublished() == null && podcasts.size() > 0) {
            channel.setLastPublished(podcasts.get(0).getPublished());
        }

        return channel;
    }


    private Instant getPublishInstant(String pubDate) {
        try {
            Date parse = simpleDateFormat.parse(pubDate);
            return Instant.ofEpochMilli(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
