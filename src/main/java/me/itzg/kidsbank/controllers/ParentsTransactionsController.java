package me.itzg.kidsbank.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import me.itzg.kidsbank.CurrencyUtil;
import me.itzg.kidsbank.KidsbankException;
import me.itzg.kidsbank.domain.Account;
import me.itzg.kidsbank.domain.Parent;
import me.itzg.kidsbank.repositories.AccountsRepository;
import me.itzg.kidsbank.services.AccountsService;
import me.itzg.kidsbank.services.ParentUserDetailsService;
import me.itzg.kidsbank.shared.TransactionSummaryPage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/parents/transactions")
public class ParentsTransactionsController {
	
	@Autowired
	AccountsService accountsService;
	
	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	private ParentUserDetailsService parentUserDetailsService;

	@RequestMapping(value="{accountId}", method=RequestMethod.GET)
	public String gotoTransactions(Principal user, Model model, @PathVariable String accountId) throws KidsbankException {
		parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		Account account = accountsRepository.findById(accountId);
		model.addAttribute("currencySymbol", CurrencyUtil.getCurrencySymbol(account.getLocale()));
		
		model.addAttribute("accountId", accountId);
		model.addAttribute("accountName", account.getName());
		
		return "/parents/transactions";
	}
	
	@RequestMapping(value="{accountId}/summaries", method=RequestMethod.GET)
	@ResponseBody
	public TransactionSummaryPage getTransactions(Principal user, Model model, 
			Pageable pageable, 
			@PathVariable String accountId) throws KidsbankException {
		parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		return accountsService.getTransactionSummaries(accountId, pageable);
	}

	@RequestMapping(value="{accountId}/suggestDescriptions", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getDescriptionSuggestions(Principal user, @PathVariable String accountId, @RequestParam String term) throws KidsbankException {
		parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		return accountsService.getDescriptionSuggestions(accountId, term);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param date
	 * @param description
	 * @param income
	 * @param expense
	 * @return
	 * @throws KidsbankException
	 */
	@RequestMapping(value="{accountId}/add", method=RequestMethod.POST)
	@ResponseBody
	public String addTransaction(Principal user, @PathVariable String accountId, 
			@RequestParam Date date, @RequestParam String description,
			@RequestParam(required=false) Double income, @RequestParam(required=false) Double expense) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		String transactionId = accountsService.addTransaction(accountId, parent, date, description, income, expense);
		
		return transactionId;
	}
	
	/**
	 * 
	 * @param accountId
	 * @param transactionId
	 * @throws KidsbankException
	 */
	@RequestMapping(value="{accountId}/{transactionId}/delete", method=RequestMethod.POST)
	public void deleteTransaction(Principal user, @PathVariable String accountId, 
			@PathVariable String transactionId, HttpServletResponse response) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		accountsService.deleteTransaction(accountId, transactionId, parent);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	/**
	 * 
	 * @param accountId
	 * @param transactionId
	 * @throws KidsbankException
	 */
	@RequestMapping(value="{accountId}/{transactionId}/edit", method=RequestMethod.POST)
	public void editTransaction(Principal user, @PathVariable String accountId, 
			@PathVariable String transactionId, @RequestParam Date date, @RequestParam String description,
			@RequestParam(required=false) Double income, @RequestParam(required=false) Double expense,
			HttpServletResponse response) throws KidsbankException {
		Parent parent = parentUserDetailsService.extractValidateParentOfAccount(user, accountId);
		
		accountsService.editTransaction(accountId, transactionId, parent, date, description, income, expense);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
}
