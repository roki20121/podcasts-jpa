package com.roman.podcastplayer.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"uuid"})
@RequiredArgsConstructor
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(unique = true)
    private String uuid;

    @NonNull
    private String title;

    private Instant published;

    private String description;

    private String audioUrl;

    private boolean starred;

    @Transient
    private boolean newItem = false;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "podcast_category",
            joinColumns = @JoinColumn(name = "podcast_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
    }

}
