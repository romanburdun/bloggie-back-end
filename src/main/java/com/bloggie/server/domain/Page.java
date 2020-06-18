package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Setter
@Getter
public class Page extends BaseEntity {

    private String title;
    private String content;
    private String slug;
    @OneToOne
    private Meta seo;
    @OneToMany
    List<CustomField> customFields;


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
