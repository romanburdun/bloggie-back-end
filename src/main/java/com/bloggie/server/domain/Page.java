package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Page extends BaseEntity {

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String slug;
    @OneToOne
    private Meta seo;
    @OneToMany
    List<CustomField> customFields;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "page_media",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id"))
    Set<Media> media;


    @Override
    public boolean equals(Object obj) {

        Page anotherPage = (Page) obj;

        if (!this.getTitle().equals(anotherPage.getTitle())) {
            return false;
        }

        if(!this.getContent().equals(anotherPage.getContent())) {
            return false;
        }

        if(!this.getSlug().equals(anotherPage.getSlug())) {
            return false;
        }

        if(!this.getSeo().equals(anotherPage.getSeo())) {
            return false;
        }

        return true;
    }
}
