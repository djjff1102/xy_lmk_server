package com.wenge.common.filter;

import com.wenge.common.constants.Constants;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author HAO灏 2020/8/7 09:48
 */
@Component
@Order(-9999)
public class LanguageFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
		//暂时不考虑国际化
		Locale locale = httpServletRequest.getLocale();
//		if (locale == null) {
//			locale = Locale.SIMPLIFIED_CHINESE;
//		}
		locale = Locale.SIMPLIFIED_CHINESE;
		Constants.CURRENT_LOCALE.set(locale);
		LocaleContextHolder.setLocale(locale, true);
		LocaleContextHolder.setDefaultLocale(locale);
		Locale.setDefault(locale);
		servletRequest.setCharacterEncoding(Charset.defaultCharset().toString());
		servletResponse.setCharacterEncoding(Charset.defaultCharset().toString());
		filterChain.doFilter(servletRequest, servletResponse);
		Constants.CURRENT_LOCALE.remove();
	}

}
