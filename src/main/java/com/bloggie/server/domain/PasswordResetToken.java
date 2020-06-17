package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class PasswordResetToken extends BaseEntity {
    String email;
    String token;
}
