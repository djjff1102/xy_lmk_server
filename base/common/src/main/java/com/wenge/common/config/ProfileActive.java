package com.wenge.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author HAO灏 2023/3/29 14:22
 */
@Getter
@Setter
@Component
@RefreshScope
public class ProfileActive {

	@Value("${profile-active}")
	private String profileActive;

	public boolean devMode() {
		return "dev".equals(this.profileActive);
	}

	public String get(String key) {
		return this.devMode() ? key + "_" + this.profileActive : key;
	}


}
