package com.roman.podcastplayer.rest.dto;

import com.roman.podcastplayer.entity.Category;
import lombok.Data;

@Data
public class CategoryDto {

    private Integer id;
    private String name;

    public CategoryDto(Category category) {
        id = category.getId();
        name = category.getName();
    }

}
