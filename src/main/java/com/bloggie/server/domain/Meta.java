package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
public class Meta extends BaseEntity {

    @NotNull
    @NotEmpty
    private String seoTitle;
    @NotNull
    @NotEmpty
    private String seoDescription;


    @Override
    public boolean equals(Object obj) {
       Meta anotherMeta = (Meta) obj;

       if(!this.getSeoTitle().equals(anotherMeta.getSeoTitle())) {
           return false;
       }

       if(!this.getSeoDescription().equals(anotherMeta.getSeoDescription())) {
           return false;
       }

        return true;
    }
}
