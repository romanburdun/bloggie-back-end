package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.SiteSettingsMapper;
import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.SiteSettings;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.SiteSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SiteSettingsServiceImpl implements SiteSettingsService {

    private final SiteSettingsRepository siteSettingsRepository;
    private final SiteSettingsMapper siteSettingsMapper;

    @Override
    public SiteSettingsDTO updateSettings(SiteSettingsDTO settingsDTO) {

        SiteSettings settings = siteSettingsRepository.findById(1L)
                .orElseThrow(()-> new ApiRequestException("Server settings not exist", HttpStatus.INTERNAL_SERVER_ERROR));

        SiteSettings updateSettings = siteSettingsMapper.siteSettingsDtoToSiteSettings(settingsDTO);

        if(!settings.equals(updateSettings)) {
            settings.setRegistrationAllowed(updateSettings.isRegistrationAllowed());
            settings.setPostsPerPage(updateSettings.getPostsPerPage());
            return siteSettingsMapper
                    .siteSettingsToSiteSettingsDto(siteSettingsRepository.save(settings));
        }

        return siteSettingsMapper.siteSettingsToSiteSettingsDto(settings);
    }

    @Override
    public SiteSettingsDTO getSettings() {

        SiteSettings settings = siteSettingsRepository.findById(1L)
                .orElseThrow(()-> new ApiRequestException("Server settings not exist", HttpStatus.INTERNAL_SERVER_ERROR));

        return siteSettingsMapper.siteSettingsToSiteSettingsDto(settings);
    }
}
