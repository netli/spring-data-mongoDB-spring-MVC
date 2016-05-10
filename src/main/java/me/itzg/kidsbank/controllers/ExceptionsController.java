package me.itzg.kidsbank.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionsController implements HandlerExceptionResolver {
	private static final Logger logger = LoggerFactory
			.getLogger(ExceptionsController.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		logger.warn("An exception was intercepted", ex);
 		return new ModelAndView("/error");
	}
}
