package com.roman.podcastplayer.rest.dto;

import com.roman.podcastplayer.entity.Channel;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChannelDto {

    private Integer id;
    private String url;
    private String title;
    private String description;
    private Instant lastPublished;
    private boolean starred;
    private List<PodcastDto> podcasts;

    public ChannelDto(Channel channel) {
        id = channel.getId();
        url = channel.getUrl();
        title = channel.getTitle();
        description = channel.getDescription();
        lastPublished = channel.getLastPublished();
        starred = channel.isStarred();

        podcasts = channel.getPodcasts().stream()
                .map(PodcastDto::new)
                .collect(Collectors.toList());
    }

}
