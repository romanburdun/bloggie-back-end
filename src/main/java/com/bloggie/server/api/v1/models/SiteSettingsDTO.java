package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SiteSettingsDTO {
    private int postsPerPage;
    private boolean registrationAllowed;
}
