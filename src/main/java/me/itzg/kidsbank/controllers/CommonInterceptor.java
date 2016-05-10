package me.itzg.kidsbank.controllers;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;

public class CommonInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
	
	private static final Pattern jsonContentTypePattern = Pattern.compile("application/json");
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		UrlPathHelper urlPathHelper = new UrlPathHelper();

		if (urlPathHelper.getPathWithinApplication(request).startsWith("/parents")) {
			request.setAttribute("kidsbankIsParent", Boolean.TRUE);
		}
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
		String contextPath = urlPathHelper.getContextPath(request);
		uriBuilder.replacePath(contextPath);
		
		String baseAbsUrl = uriBuilder.build().toUriString();
		request.setAttribute("myBaseAbsUrl", baseAbsUrl);

		// Consumers will want to append a path with a leading slash, so
		// a context path in the root context would result in two slashes
		if (contextPath.equals("/")) {
			contextPath = "";
		}
		request.setAttribute("myBaseUrl", contextPath);
		
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String contentType = response.getContentType();
		logger.debug("Content type is {}", response.getContentType());

		if (contentType != null) {
			if (jsonContentTypePattern.matcher(contentType).lookingAt()) {
				response.setHeader("Expires", "0");
			}
		}
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
