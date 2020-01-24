package com.roman.podcastplayer.rest;

import com.roman.podcastplayer.manage.ChannelManager;
import org.glassfish.jersey.internal.inject.AbstractBinder;

public class PodcastBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(ChannelManager.class).to(ChannelManager.class);
    }
}
