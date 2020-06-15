package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class SiteSettings extends BaseEntity {

    private int postsPerPage;
    private boolean registrationAllowed;


    @Override
    public boolean equals(Object obj) {

        SiteSettings anotherSettings = (SiteSettings) obj;

        if(this.getPostsPerPage() != anotherSettings.getPostsPerPage()) {
            return false;
        }

        if(this.isRegistrationAllowed() != anotherSettings.isRegistrationAllowed()) {
            return false;
        }

        return true;
    }
}
