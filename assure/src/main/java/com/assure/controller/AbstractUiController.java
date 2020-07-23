package com.assure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class AbstractUiController {

	// Gets value of base Url from properties file
	@Value("${app.baseUrl}")
	private String baseUrl;

	protected ModelAndView mav(String page) {
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("baseUrl", baseUrl);
		return mav;
	}

}
