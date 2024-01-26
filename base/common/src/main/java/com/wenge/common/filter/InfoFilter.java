package com.wenge.common.filter;


import com.wenge.common.constants.Constants;
import com.wenge.common.util.CommonUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author HAO灏 2020/8/26 15:05
 */
@Component
@Order(2)
public class InfoFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
		this.setRemoteHost(httpServletRequest);
		this.setTraceId(httpServletRequest);
		String dataPermissionCode = httpServletRequest.getParameter("data_permission_code");
		if (CommonUtil.isNotBlank(dataPermissionCode)) {
			Constants.REQUEST_DATA_PERMISSION_CODE.set(dataPermissionCode);
		}
		filterChain.doFilter(servletRequest, servletResponse);
		Constants.REMOTE_REQUEST_IP.remove();
		Constants.TRACE_ID.remove();
		Constants.REQUEST_DATA_PERMISSION_CODE.remove();
	}

	private void setTraceId(HttpServletRequest httpServletRequest) {
		String traceId = httpServletRequest.getHeader("TRACE_ID");
		if (CommonUtil.isBlank(traceId)) {
			return;
		}
		Constants.TRACE_ID.set(traceId);
	}

	private void setRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (CommonUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (CommonUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (CommonUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		Constants.REMOTE_REQUEST_IP.set(ip);
	}

}
