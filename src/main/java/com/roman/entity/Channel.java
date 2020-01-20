package com.roman.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String url;

    @NonNull
    private String title;

    private String description;

    @Column(name = "last_published")
    private Instant lastPublished;

    private boolean starred;

    @OneToMany(mappedBy = "channel")
    private List<Podcast> podcasts = new LinkedList<>();

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
        podcast.setChannel(this);
    }
}
