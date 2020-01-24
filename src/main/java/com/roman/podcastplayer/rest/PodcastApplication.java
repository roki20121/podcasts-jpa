package com.roman.podcastplayer.rest;

import org.glassfish.jersey.server.ResourceConfig;

public class PodcastApplication extends ResourceConfig {
    public PodcastApplication() {
        register(new PodcastBinder());
        packages(true, "com.roman.podcastplayer.rest");
        packages(true, "com.roman.podcastplayer.manage");
    }
}
