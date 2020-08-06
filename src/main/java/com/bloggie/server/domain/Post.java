package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Post extends BaseEntity {

    private LocalDateTime datePublished;
    private String title;
    private String cover;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String excerpt;
    private String slug;
    private boolean draft;
    private int readTime;
    @OneToOne
    private Meta seo;
    @OneToOne
    private User author;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_media",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id"))
    Set<Media> media;


    @Override
    public boolean equals(Object obj) {

        Post anotherPost = (Post) obj;

        if(!this.getTitle().equals(anotherPost.getTitle())) {
            return false;
        }

        if(!this.getCover().equals(anotherPost.getCover())) {
            return false;
        }

        if(!this.getContent().equals(anotherPost.getContent())) {
            return false;
        }

        if(!this.getExcerpt().equals(anotherPost.getExcerpt())) {
            return false;
        }

        if(!this.getSlug().equals(anotherPost.getSlug())) {
            return false;
        }

        if(!this.getSeo().equals(anotherPost.getSeo())) {
            return false;
        }

        if(this.getReadTime() != anotherPost.getReadTime()) {
            return false;
        }

        if(this.isDraft() != anotherPost.isDraft()) {
            return false;
        }

        return true;
    }
}
