package me.itzg.kidsbank.controllers;

import me.itzg.kidsbank.commands.KidRegistration;
import me.itzg.kidsbank.services.KidLinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/kids/")
@SessionAttributes("kidRegistration")
public class KidsController {
	
	@Autowired
	private KidLinkService kidLinkService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String kidsDefault() {
		return "/kids/kids";
	}
	
	@ModelAttribute 
	public KidRegistration createDefault() {
		return new KidRegistration();
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView handleInitialLogin(@CookieValue(value="COMPUTERID", required=false) String computerId) {
		return loginDecider(computerId, null);
	}
	
	/**
	 * 
	 * @param computerId a cookie tracking the computer (browser instance specifically)
	 * @param registration incrementally populated as the registration process continues
	 * @return model populated with registration
	 */
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView handleFormLogin(@CookieValue(value="COMPUTERID", required=false) String computerId,
			@ModelAttribute KidRegistration registration, @RequestParam(value="ready", required=false) Boolean ready) {
		return loginDecider(computerId, registration);
	}
	
	private ModelAndView  loginDecider(String computerId,
			KidRegistration registration) {
		if (computerId == null) {
			if (registration == null) {
				return new ModelAndView("/kids/loginUnknownComputer");
			}
			else if (registration.hasValidName()) {
				
			}
			else {
				ModelAndView modelAndView = new ModelAndView("/kids/loginUnknownComputer");
				modelAndView.addObject("errorCode", "errors.kids.invalid-name");
				return modelAndView;
			}
		}
		// TODO: make deeper decision based on pending kid link request, etc
		return new ModelAndView("/kids/loginKnownComputer");

	}

}
