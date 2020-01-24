package com.roman.podcastplayer.rest.dto;

import com.roman.podcastplayer.entity.Podcast;
import lombok.Data;

import java.time.Instant;

@Data
public class PodcastDto {

    private Integer id;
    private String uuid;
    private String title;
    private Instant published;
    private String description;
    private String audioUrl;
    private boolean starred;

    public PodcastDto(Podcast podcast) {
        id = podcast.getId();
        uuid = podcast.getUuid();
        title = podcast.getTitle();
        description = podcast.getDescription();
        published = podcast.getPublished();
        audioUrl = podcast.getAudioUrl();
        starred = podcast.isStarred();
    }
}
