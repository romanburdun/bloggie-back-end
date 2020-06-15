package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.services.SiteSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/site")
@AllArgsConstructor
public class SiteSettingsController {

    private final SiteSettingsService siteSettingsService;

    @PutMapping
    public SiteSettingsDTO updateSettings(@RequestBody SiteSettingsDTO settingsDTO) {
        return siteSettingsService.updateSettings(settingsDTO);
    }

    @GetMapping
    public SiteSettingsDTO getSettings() {
        return siteSettingsService.getSettings();
    }
}
