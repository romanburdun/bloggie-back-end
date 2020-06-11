package com.bloggie.server.security.requests;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String name;
    private String email;
    private String password;
}
