package com.bloggie.server.fixtures;

import com.bloggie.server.domain.SiteSettings;

public abstract class SettingsFixtures {

    public static SiteSettings getSettings() {
        SiteSettings settings = new SiteSettings();

        settings.setRegistrationAllowed(true);
        settings.setPostsPerPage(5);

        return settings;
    }
}
