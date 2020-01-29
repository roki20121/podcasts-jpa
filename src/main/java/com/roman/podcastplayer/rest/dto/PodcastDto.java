package com.roman.podcastplayer.rest.dto;

import com.roman.podcastplayer.entity.Podcast;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class PodcastDto {

    private Integer id;
    private String uuid;
    private String title;
    private Instant published;
    private String description;
    private String audioUrl;
    private boolean starred;
    private boolean newItem;
    private Set<CategoryDto> categoryDtos;


    public PodcastDto(Podcast podcast) {
        id = podcast.getId();
        uuid = podcast.getUuid();
        title = podcast.getTitle();
        description = podcast.getDescription();
        published = podcast.getPublished();
        audioUrl = podcast.getAudioUrl();
        starred = podcast.isStarred();
        newItem = podcast.isNewItem();
        categoryDtos = podcast.getCategories()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toSet());
    }
}
