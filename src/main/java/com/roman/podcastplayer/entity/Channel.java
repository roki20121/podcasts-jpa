package com.roman.podcastplayer.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "get all Channels", query = "select c from Channel c")
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

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "channel", cascade = CascadeType.REMOVE)
    private List<Podcast> podcasts = new LinkedList<>();


    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts.clear();
        podcasts.forEach(this::addPodcast);
    }

    public void addPodcast(Podcast podcast) {
        podcasts.add(podcast);
        podcast.setChannel(this);
    }
}
