package com.wenge.common.config.mapstruct;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * @author HAO灏 2020/11/18 20:04
 */
@MapperConfig(uses = {StringConverter.class, DateConverter.class},
		unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class MapStructConfig {
}
