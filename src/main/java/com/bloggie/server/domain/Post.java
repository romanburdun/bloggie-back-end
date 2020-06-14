package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Post extends BaseEntity {

    private LocalDateTime datePublished;
    private String title;
    private String cover;
    private String content;
    private String excerpt;
    private String slug;
    private int readTime;
    @OneToOne
    private Meta seo;
    @OneToOne
    private User author;


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

        return true;
    }
}
