package com.bloggie.server.security.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class SignupRequest {
    private String name;
    private String email;
    private String password;
}
