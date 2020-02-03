package com.roman.podcastplayer.rest.dto;

import com.roman.podcastplayer.entity.Category;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.entity.PodcastUtils;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PodcastDtoTest {

    @Test
    void constructor_podcast_podcastDto() {
        Podcast podcast = PodcastUtils.generateRandomPodcast(3);

        PodcastDto podcastDto = new PodcastDto(podcast);

        assertEquals(podcast.getUuid(), podcastDto.getUuid());
        assertEquals(podcast.getId(), podcastDto.getId());
        assertEquals(podcast.getTitle(), podcastDto.getTitle());
        assertEquals(podcast.getDescription(), podcastDto.getDescription());
        assertEquals(podcast.getPublished(), podcastDto.getPublished());
        assertEquals(podcast.isStarred(), podcastDto.isStarred());
        assertEquals(podcast.isNewItem(), podcastDto.isNewItem());
        assertEquals(podcast.getAudioUrl(), podcastDto.getAudioUrl());

        Set<Category> categories = podcast.getCategories();
        Set<CategoryDto> categoryDtos = podcastDto.getCategoryDtos();
        assertEquals(categories.size(), categoryDtos.size());

        Iterator<Category> catIter = categories.iterator();
        Iterator<CategoryDto> dtoIter = categoryDtos.iterator();
        while (catIter.hasNext()) {
            String expected = catIter.next().getName();
            String actual = dtoIter.next().getName();
            assertEquals(expected, actual);
        }
    }

}