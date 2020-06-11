package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Setter
@Getter
public class Role extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    private RoleName name;
}
