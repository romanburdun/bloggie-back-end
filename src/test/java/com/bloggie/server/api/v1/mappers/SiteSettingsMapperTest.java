package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.SiteSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SiteSettingsMapperTest {

    private final SiteSettingsMapper siteSettingsMapper = SiteSettingsMapper.INSTANCE;

    @Test
    void siteSettingsDtoToSiteSettings() {

        SiteSettingsDTO siteSettingsDTO = new SiteSettingsDTO();
        siteSettingsDTO.setPostsPerPage(5);
        siteSettingsDTO.setRegistrationAllowed(true);

        SiteSettings settings = siteSettingsMapper.siteSettingsDtoToSiteSettings(siteSettingsDTO);

        assertNotNull(settings);
        assertEquals(true, settings.isRegistrationAllowed());
        assertEquals(5, settings.getPostsPerPage());

    }

    @Test
    void siteSettingsToSiteSettingsDto() {

        SiteSettings settings = new SiteSettings();
        settings.setPostsPerPage(3);
        settings.setRegistrationAllowed(false);

        SiteSettingsDTO settingsDTO = siteSettingsMapper.siteSettingsToSiteSettingsDto(settings);

        assertNotNull(settingsDTO);
        assertEquals(false, settingsDTO.isRegistrationAllowed());
        assertEquals(3, settingsDTO.getPostsPerPage());
    }
}
