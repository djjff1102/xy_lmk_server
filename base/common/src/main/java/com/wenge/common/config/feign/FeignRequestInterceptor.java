package com.wenge.common.config.feign;


import com.wenge.common.constants.AuthConstants;
import com.wenge.common.constants.Constants;
import com.wenge.common.util.CommonUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HAO灏 2020/9/30 17:07
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

	//@Resource
	//private TextEncryptor textEncryptor;

	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			if (CommonUtil.isNotBlank(request.getHeader("Cookie"))) {
				template.header("Cookie", request.getHeader("Cookie"));
			}
		}
		template.header("TRACE_ID", Constants.TRACE_ID.get());
		if (AuthConstants.getCurrentUser() == null) {
			return;
		}
		template.header("CURRENT_USER_ID", AuthConstants.getCurrentUser().getId() + "");
	}
}
