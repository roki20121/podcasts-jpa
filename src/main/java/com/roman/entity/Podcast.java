package com.roman.entity;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class Podcast {

    @NonNull
    private String uuid;
    @NonNull
    private String title;
    @NonNull
    private LocalDateTime published;
    private String description;
    private String audioUrl;
    private boolean starred;

}
