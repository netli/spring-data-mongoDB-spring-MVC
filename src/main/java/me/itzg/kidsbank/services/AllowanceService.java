package me.itzg.kidsbank.services;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import me.itzg.kidsbank.domain.Account;
import me.itzg.kidsbank.domain.AllowanceSpec;
import me.itzg.kidsbank.domain.Transaction;
import me.itzg.kidsbank.repositories.TransactionsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class AllowanceService {
	private static final Logger logger = LoggerFactory
			.getLogger(AllowanceService.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private TransactionsRepository transactionsRepository;
	
	private TimeZone timeZone = TimeZone.getTimeZone("America/Chicago");
	
	public void processDailyAllowance() {
		Query acctQuery = Query.query(Criteria.where("allowance").exists(true));
		acctQuery.fields().include("id").include("allowance");
		List<Account> accountsWithAllowance = 
				mongoTemplate.find(acctQuery, Account.class);
		
		Calendar cal = Calendar.getInstance(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		for (Account account : accountsWithAllowance) {
			final AllowanceSpec allowanceForAccount = account.getAllowance();
			
			// VERY IMPORTANT -- is the allowance for today?
			if (allowanceForAccount.getDayOfWeek() == cal.get(Calendar.DAY_OF_WEEK)) {
				
				// Craft a query that matches an existing allowance transaction for today, same amount, etc
				Criteria criteria = Criteria.where("when").is(cal.getTime())
						.and("accountId").is(account.getId())
						.and("amount").is(allowanceForAccount.getAmount())
						.and("description")
						.is(allowanceForAccount.getDescription())
						.and("parentId").is(allowanceForAccount.getParentId());
				
				// Leverage the upsert operation to ensure only one of each allowance spec is 
				// inserted per day. Nothing else to update, just insert the query's content.
				logger.debug("Upserting allowance transaction {}", criteria);
				mongoTemplate.upsert(Query.query(criteria), Update.update(
						"amount", allowanceForAccount.getAmount()),
						Transaction.class);
			} else {
				logger.debug("Found allowance entry, but not for today: {} {}", account, allowanceForAccount);
			}
		}
	}

	public String getTimeZone() {
		return timeZone.getID();
	}

	public void setTimeZone(String timeZoneID) {
		this.timeZone = TimeZone.getTimeZone(timeZoneID);
	}
}
