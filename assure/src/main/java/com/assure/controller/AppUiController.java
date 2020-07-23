package com.assure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ui")
public class AppUiController extends AbstractUiController {

	// Controller which returns model and view of given name of html file

	@RequestMapping(value = "/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/client")
	public ModelAndView client() {
		return mav("client.html");
	}

	@RequestMapping(value = "/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/order")
	public ModelAndView order() {
		return mav("order.html");
	}
}
