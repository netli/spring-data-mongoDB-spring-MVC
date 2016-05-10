package me.itzg.kidsbank.controllers;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.itzg.kidsbank.shared.CountryResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	public static final String COOKIE_CHOICE = "choice";
	
	private static enum GoTarget {
		PARENT,
		KID
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, @CookieValue(value=COOKIE_CHOICE, required=false) String choice) {
		if (choice == null) {
			return "choose";
		}
		else {
			GoTarget target = GoTarget.valueOf(choice);
			if (target == null) {
				return "choose";
			}
			else {
				return formChoiceRedirect(target);
			}
		}
	}

	private String formChoiceRedirect(GoTarget target) {
		return "redirect:/"+target.name().toLowerCase()+"s/";
	}
	
	@RequestMapping("/go")
	public String goDownPath(HttpServletRequest request, HttpServletResponse response, @RequestParam String to) {
		GoTarget target = GoTarget.valueOf(to.toUpperCase());
		if (target == null) {
			return "redirect:/";
		}
		else {
			CookieGenerator cookieGenerator = new CookieGenerator();
			cookieGenerator.setCookieName(COOKIE_CHOICE);
			cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
			cookieGenerator.setCookiePath(request.getContextPath());
			cookieGenerator.addCookie(response, target.name());
			return formChoiceRedirect(target);
		}
	}
	
	@RequestMapping("/j/{kidLinkCode}")
	public String handleShortUrlJoin(@PathVariable String kidLinkCode) {
		return "redirect:/parents/accounts/join/{kidLinkCode}";
	}
	
	@RequestMapping("/logout")
	public String handleLogout() {
		// Redirect to the appropriate logout URL
		return "redirect:/j_spring_security_logout";
	}
	
	@RequestMapping("/api/countries")
	public @ResponseBody Collection<CountryResponse> getCountries(Locale userLocale) {
		HashMap<String, CountryResponse> byCountry = new HashMap<String, CountryResponse>();
		Locale[] availableLocales = Locale.getAvailableLocales();
		for (Locale locale : availableLocales) {
			String displayCountry = locale.getDisplayCountry(userLocale);
			// Give preference to the current user's locale-language, so always
			// store the locale of a given country that has the same language as the user's.
			// Otherwise, store the first locale encountered.
			if (locale.getLanguage().equals(userLocale.getLanguage()) || !byCountry.containsKey(displayCountry)) {
				if (!displayCountry.isEmpty()) {
					byCountry.put(displayCountry, new CountryResponse(
							displayCountry, locale.toString(), locale.equals(userLocale)));
				}
			}
		}
		
		Set<CountryResponse> results = new TreeSet<CountryResponse>(new Comparator<CountryResponse>() {
			public int compare(CountryResponse o1, CountryResponse o2) {
				return o1.getDisplayCountry().compareTo(o2.getDisplayCountry());
			}
		});
		results.addAll(byCountry.values());
		return results;
	}
}
