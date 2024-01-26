package com.wenge.datasource.mysql;


import com.wenge.datasource.base.PartitionManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author HAO灏 2020/8/26 15:05
 */
@Component
@Order(5)
public class PartitionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(servletRequest, servletResponse);
		PartitionManager.clearPartition();
	}

}
