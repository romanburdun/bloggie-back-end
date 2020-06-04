package com.bloggie.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AsJsonController {
    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
