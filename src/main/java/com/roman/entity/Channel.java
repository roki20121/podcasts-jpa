package com.roman.entity;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class Channel {

    @NonNull
    private String channelUrl;
    @NonNull
    private String title;
    private String description;
    private LocalDateTime lastPublishDate;
    private boolean starred;

}
