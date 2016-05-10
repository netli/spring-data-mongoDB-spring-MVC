package me.itzg.kidsbank.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import me.itzg.kidsbank.CurrencyUtil;
import me.itzg.kidsbank.KidsbankException;
import me.itzg.kidsbank.ParentUserDetails;
import me.itzg.kidsbank.domain.Account;
import me.itzg.kidsbank.domain.AllowanceSpec;
import me.itzg.kidsbank.domain.KidLink;
import me.itzg.kidsbank.domain.Parent;
import me.itzg.kidsbank.repositories.AccountsRepository;
import me.itzg.kidsbank.services.AccountsService;
import me.itzg.kidsbank.services.KidLinkService;
import me.itzg.kidsbank.services.ParentUserDetailsService;
import me.itzg.kidsbank.shared.AccountSummary;
import me.itzg.kidsbank.shared.KidLinkShare;
import me.itzg.kidsbank.shared.LocaleSpec;
import me.itzg.kidsbank.shared.ParentSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/parents/")
public class ParentsController {
	
	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	private AccountsRepository accountsRepository;
	
	@Autowired
	private KidLinkService kidLinkService;
	
	@Autowired
	private ParentUserDetailsService parentUserDetailsService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String parentsDefault() {
		return "/parents/main";
	}
	
	@RequestMapping(value="accounts", method=RequestMethod.GET)
	@ResponseBody
	public List<AccountSummary> getAccounts(Principal user) throws KidsbankException {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);
		return accountsService.getAccountSummaries(parent);
	}
	
	@RequestMapping(value="accounts/{accountId}/balance", method=RequestMethod.GET)
	@ResponseBody
	public String getAccountBalance(Principal user, @PathVariable String accountId, HttpServletResponse response) throws KidsbankException {
		parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		double balance = accountsService.getAccountBalance(accountId);
		
		Account account = accountsRepository.findById(accountId);

		// don't cache
		response.setHeader("Expires", "0");
		
		return CurrencyUtil.format(account.getLocale(), balance);
	}
	
	@RequestMapping(value="accounts/add", method=RequestMethod.POST)
	public void addAccount(Locale locale, Principal user, @RequestParam String name, 
			@RequestParam int day,
			@RequestParam(required=false) String description,
			@RequestParam(required=false) Double amount, HttpServletResponse response) throws KidsbankException {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);
		Account account = accountsService.createAccount(locale, parent, name);
		
		if (account == null) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return;
		}
		
		if (parent.getAccounts() == null) {
			parent.setAccounts(new ArrayList<String>());
		}
		parent.getAccounts().add(account.getId());
		
		if (day != 0) {
			accountsService.configureAllowance(parent, account.getId(), day, amount, description);
		}
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(value="accounts/{accountId}/detach", method=RequestMethod.POST)
	@ResponseBody
	public String detachAccount(Principal user, @PathVariable String accountId) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		accountsService.detachAccount(accountId, parent);
		
		return "success";
	}
	
	@RequestMapping(value="accounts/{accountId}/currencySymbol", method=RequestMethod.GET)
	@ResponseBody
	public String getAccountCurrencySymbol(Principal user, @PathVariable String accountId) throws KidsbankException {
		Account account = accountsRepository.findById(accountId);

		return Currency.getInstance(account.getLocale().toLocale()).getSymbol();
	}
	
	@RequestMapping(value="accounts/{accountId}/edit", method=RequestMethod.POST)
	@ResponseBody
	public String editAccount(Principal user, @PathVariable String accountId, 
			@RequestParam(required=false) String name) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		if (name != null) {
			accountsService.rename(accountId, parent, name);
		}
		
		return "success";
	}
	
	/**
	 * 
	 * @param user
	 * @param accountId
	 * @param day 0 = off, 1 = Sunday, 7 = Saturday
	 * @param amount
	 * @param response
	 * @throws KidsbankException
	 */
	@RequestMapping(value="accounts/{accountId}/configureAllowance", method=RequestMethod.POST)
	public void configureAllowance(Principal user, @PathVariable String accountId, 
			@RequestParam int day,
			@RequestParam(required=false) String description,
			@RequestParam(required=false) Double amount, HttpServletResponse response) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		if (day == 0) {
			accountsService.deactiveAllowance(parent, accountId);
		}
		else {
			accountsService.configureAllowance(parent, accountId, day, amount, description);
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(value="accounts/{accountId}/allowance", method=RequestMethod.GET)
	@ResponseBody
	public AllowanceSpec getAllowance(Principal user, @PathVariable String accountId) throws KidsbankException {
		parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		return accountsService.getAllowance(accountId);
	}

	@RequestMapping(value="accounts/share", method=RequestMethod.GET)
	@ResponseBody
	public KidLinkShare share(Principal user, WebRequest webRequest, UriComponentsBuilder uriBuilder) throws KidsbankException {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);
		
		KidLink kidLink = kidLinkService.createKidLink(parent.getOpenId());
		
		KidLinkShare share = new KidLinkShare();
		share.setCode(kidLink.getCode());
		
		
		uriBuilder.replacePath(webRequest.getContextPath()+"/j/"+kidLink.getCode());
		share.setJoinUrl(uriBuilder.build().toUriString());
		
		return share;
	}


	@RequestMapping(value="accounts/join/{kidLinkCode}", method=RequestMethod.GET)
	public String join(Principal user, @PathVariable int kidLinkCode) throws KidsbankException {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);
		
		kidLinkService.joinKidLink(parent.getOpenId(), kidLinkCode);
		
		return "redirect:/parents/";
	}
	
	@RequestMapping(value="settings", method=RequestMethod.GET)
	public @ResponseBody ParentSettings getSettings(Principal user) {
		Parent liveParent = parentUserDetailsService.loadLive(ParentUserDetails.extractFromPrincipal(user));
		ParentSettings settings = new ParentSettings();
		settings.setNickname(liveParent.getNickname());
		return settings;
	}

	/**
	 * 
	 * @param yourName the name your kids use to refer to you
	 * @param localeCode a Locale compliant country_language code
	 */
	@RequestMapping(value="settings", method=RequestMethod.POST)
	public void applySettings(Principal user, 
			@RequestParam(required=false) String nickname, 
			@RequestParam String localeCode, HttpServletResponse response) {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);
		
		accountsService.changeAccountLocales(parent, LocaleSpec.parseLocaleCode(localeCode));
		
		parentUserDetailsService.setNickname(parent, nickname);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
