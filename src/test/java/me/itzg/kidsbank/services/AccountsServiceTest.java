package me.itzg.kidsbank.services;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static me.itzg.kidsbank.MongoMatchers.*;
import me.itzg.kidsbank.domain.Account;
import me.itzg.kidsbank.domain.Parent;
import me.itzg.kidsbank.domain.Transaction;
import me.itzg.kidsbank.repositories.AccountsRepository;
import me.itzg.kidsbank.repositories.ParentsRepository;
import me.itzg.kidsbank.repositories.TransactionsRepository;
import me.itzg.kidsbank.services.AccountsService;
import me.itzg.kidsbank.shared.LocaleSpec;

import org.easymock.IAnswer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AccountsServiceTest {
	
	@Configuration
	static class Config {
		@Bean
		public AccountsService accountsService() {
			return new AccountsService();
		}
		
		@Bean
		public ParentsRepository parentsRepository() {
			return createMock(ParentsRepository.class);
		}
		
		@Bean
		public AccountsRepository accountsRepository() {
			return createMock(AccountsRepository.class);
		}
		
		@Bean
		public TransactionsRepository transactionsRepository() {
			return createMock(TransactionsRepository.class);
		}
		
		@Bean
		public MongoTemplate mongoTemplate() {
			MongoTemplate mock = createMock(MongoTemplate.class);
			mock.setApplicationContext((ApplicationContext) anyObject());
			expect(mock.collectionExists(Transaction.class)).andStubReturn(true);
			replay(mock);
			return mock;
		}
	}
	
	@Autowired
	AccountsService accountsService;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	TransactionsRepository transactionsRepository;
	
	@Autowired
	ParentsRepository parentsRepository;
	
	@Autowired
	AccountsRepository accountsRepository;
	
	@Test
	public void testAddTransaction() throws Exception {
		Account account = new Account();
		ReflectionTestUtils.setField(account, "id", "2");
		
		Parent parent = new Parent();
		ReflectionTestUtils.setField(parent, "openId", "3");
		
		Date date = new Date();
		String description = "";
		Double income = 1.25;
		Double expense = null;
		
		expect(transactionsRepository.save(anyObject(Transaction.class))).andAnswer(new IAnswer<Transaction>() {
			public Transaction answer() throws Throwable {
				Object[] args = getCurrentArguments();
				Transaction tx = (Transaction) args[0];
				// This our only chance to do some assertions of the transaction it'll try to store
				assertEquals("2", tx.getAccountId());
				assertEquals("3", tx.getParentId());
				assertEquals(1.25, tx.getAmount(), 0.001);
				ReflectionTestUtils.setField(tx, "id", "4");
				return tx;
			}
		});
		
		replay(transactionsRepository);
		
		String newTxId = accountsService.addTransaction(account, parent, date, description, income, expense);
		
		assertEquals(newTxId, "4");
	}
	
	@Test
	public void testChangeAccountLocales() {
		reset(parentsRepository, mongoTemplate);

		Parent cachedParent = new Parent("2", new Date());
		List<String> accounts = Arrays.asList("3", "5");
		cachedParent.setAccounts(accounts);
		
		expect(parentsRepository.findOne("2")).andReturn(cachedParent);
		expect(parentsRepository.save(anyObject(Parent.class))).andReturn(cachedParent);
		
		final Query expectedQuery = Query.query(Criteria.where("_id").in(accounts));
		
		expect(mongoTemplate.updateMulti(eqQuery(expectedQuery), anyObject(Update.class), eq(Account.class)))
			.andReturn(null);
		
		replay(parentsRepository, mongoTemplate);
		
		// ...and the test
		accountsService.changeAccountLocales(cachedParent, Locale.FRENCH);
		assertNotNull(cachedParent.getDefaultLocale());
	}
	
	@Test
	public void testCreateAccountNoDefaultLocale() {
		reset(parentsRepository, accountsRepository, mongoTemplate);
		
		Parent parent = new Parent("2", new Date());
		final Locale browserLocale = new Locale("en", "GB");
		
		expect(parentsRepository.findOne("2")).andReturn(parent);
		
		expect(accountsRepository.save(anyObject(Account.class))).andAnswer(new IAnswer<Account>() {
			public Account answer() throws Throwable {
				Object[] args = getCurrentArguments();
				Account account = (Account) args[0];
				assertEquals("name1", account.getName());
				assertEquals(new LocaleSpec(browserLocale), account.getLocale());
				
				ReflectionTestUtils.setField(account, "id", "5");
				return account;
			}
		});
		
		expect(mongoTemplate.updateFirst(anyObject(Query.class), anyObject(Update.class), eq(Parent.class))).andReturn(null);
		
		replay(parentsRepository, accountsRepository, mongoTemplate);
		
		accountsService.createAccount(browserLocale, parent, "name1");
	}
	
	@Test
	public void testCreateAccountWithDefaultLocale() {
		reset(parentsRepository, accountsRepository, mongoTemplate);
		
		Parent parent = new Parent("2", new Date());
		parent.setDefaultLocale(new LocaleSpec(new Locale("es", "SP")));
		final Locale browserLocale = new Locale("en", "GB");
		
		expect(parentsRepository.findOne("2")).andReturn(parent);
		
		expect(accountsRepository.save(anyObject(Account.class))).andAnswer(new IAnswer<Account>() {
			public Account answer() throws Throwable {
				Object[] args = getCurrentArguments();
				Account account = (Account) args[0];
				assertEquals("name1", account.getName());
				assertEquals(new LocaleSpec(new Locale("es", "SP")), account.getLocale());
				
				ReflectionTestUtils.setField(account, "id", "5");
				return account;
			}
		});
		
		expect(mongoTemplate.updateFirst(anyObject(Query.class), anyObject(Update.class), eq(Parent.class))).andReturn(null);
		
		replay(parentsRepository, accountsRepository, mongoTemplate);
		
		accountsService.createAccount(browserLocale, parent, "name1");
	}
}
