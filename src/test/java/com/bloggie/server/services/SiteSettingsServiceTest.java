package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.SiteSettingsMapper;
import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.SiteSettings;
import com.bloggie.server.fixtures.SettingsFixtures;
import com.bloggie.server.repositories.SiteSettingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.MockInjection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class SiteSettingsServiceTest {

    @Mock
    private SiteSettingsRepository siteSettingsRepository;

    @Mock
    private SiteSettingsService siteSettingsService;

    private final SiteSettingsMapper siteSettingsMapper = SiteSettingsMapper.INSTANCE;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        siteSettingsService = new SiteSettingsServiceImpl(siteSettingsRepository, siteSettingsMapper);
    }

    @Test
    void updateSettings() {

        SiteSettingsDTO update = new SiteSettingsDTO();
        update.setRegistrationAllowed(true);
        update.setPostsPerPage(5);

        Mockito.when(siteSettingsRepository.findById(anyLong())).thenReturn(Optional.of(SettingsFixtures.getSettings()));
        Mockito.when(siteSettingsRepository.save(any(SiteSettings.class))).thenReturn(SettingsFixtures.getSettings());

        SiteSettingsDTO settingsDTO = siteSettingsService.updateSettings(update);

        assertNotNull(settingsDTO);
        assertEquals(5, settingsDTO.getPostsPerPage());
        assertTrue(settingsDTO.isRegistrationAllowed());

    }

    @Test
    void getSettings() {

        Mockito.when(siteSettingsRepository.findById(anyLong())).thenReturn(Optional.of(SettingsFixtures.getSettings()));

        SiteSettingsDTO settingsDTO = siteSettingsService.getSettings();

        assertNotNull(settingsDTO);
        assertEquals(5, settingsDTO.getPostsPerPage());
        assertTrue(settingsDTO.isRegistrationAllowed());
    }
}
