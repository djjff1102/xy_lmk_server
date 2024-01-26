package com.wenge.redis;

import com.alibaba.fastjson2.JSON;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.util.CommonUtil;
import com.wenge.dto.user.UserDTO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HAO灏 2022/11/17 09:21
 */
@Component
@Order(4)
public class CurrentUserInfoFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		this.setCurrentUserInfo(httpServletRequest);
		filterChain.doFilter(servletRequest, servletResponse);
		AuthConstants.remove();
	}

	private void setCurrentUserInfo(HttpServletRequest request) {
		String currentUserId = request.getHeader("CURRENT_USER_ID");
		if (CommonUtil.isBlank(currentUserId)) {
			return;
		}
		String userJson = RedisUtil.get(String.format(AuthConstants.LOGIN_USER, currentUserId));
		if (CommonUtil.isBlank(userJson)) {
			return;
		}
		AuthConstants.setCurrentUser(JSON.parseObject(userJson, UserDTO.class));
		AuthConstants.CURRENT_TENANT_ID.set(AuthConstants.getCurrentUser().getTenantId());
	}

}
