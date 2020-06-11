package com.bloggie.server.security.requests;

import lombok.Getter;

@Getter
public class AuthRequest {
    private String email;
    private String password;
}
