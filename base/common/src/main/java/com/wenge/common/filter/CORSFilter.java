package com.wenge.common.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by li on 2016/10/14.
 * 处理跨域访问的过滤器
 */
@Component
@Order(1)
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, timestamp, accessKey, sign, token");
        response.setHeader("Access-Control-Request-Headers", "token");
        response.setHeader("Access-Control-Expose-Headers", "server-timestamp");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
