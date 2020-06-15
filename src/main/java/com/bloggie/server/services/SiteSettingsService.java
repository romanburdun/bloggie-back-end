package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.SiteSettingsDTO;

public interface SiteSettingsService {
    SiteSettingsDTO updateSettings(SiteSettingsDTO settingsDTO);
    SiteSettingsDTO getSettings(SiteSettingsDTO settingsDTO);
}
