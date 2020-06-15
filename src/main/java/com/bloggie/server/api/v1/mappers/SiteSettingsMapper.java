package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.SiteSettings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteSettingsMapper {
    SiteSettingsMapper INSTANCE = Mappers.getMapper(SiteSettingsMapper.class);


    SiteSettings siteSettingsDtoToSiteSettings(SiteSettingsDTO settingsDTO);
    SiteSettingsDTO siteSettingsToSiteSettingsDto(SiteSettings settings);
}
