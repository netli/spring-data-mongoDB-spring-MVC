package me.itzg.kidsbank.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.itzg.kidsbank.KidsbankException;
import me.itzg.kidsbank.domain.KidLink;
import me.itzg.kidsbank.domain.Parent;
import me.itzg.kidsbank.repositories.KidLinkRepository;
import me.itzg.kidsbank.repositories.ParentsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class KidLinkService {
	private static final Logger logger = LoggerFactory
			.getLogger(KidLinkService.class);

	private static final Random rand = new Random();
	
	@Autowired
	private KidLinkRepository kidLinkRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ParentsRepository parentsRepository;
	
	/**
	 * hours
	 */
	private int maxKidLinkAge = 48;
	
	public KidLink createKidLink(String parentId) throws KidsbankException {
		purgeKidLinks();
		
		Parent parent = parentsRepository.findOne(parentId);
		if (parent == null) { 
			throw new KidsbankException("Unable to locate parent");
		}
		
		KidLink link = new KidLink();
		
		int code = rand.nextInt(9000)+1000;
		while (kidLinkRepository.findOne(code) != null) {
			code = rand.nextInt(9000)+1000;
		}
		
		link.setCode(code);
		link.setCreated(new Date());
		link.setAccounts(parent.getAccounts());
		return kidLinkRepository.save(link);
	}
	
	private void purgeKidLinks() {
		if (maxKidLinkAge <= 0) {
			logger.warn("Purging disabled");
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -maxKidLinkAge);
		
		mongoTemplate.remove(Query.query(Criteria.where("created").lt(cal.getTime())), KidLink.class);
	}

	public void joinKidLink(String parentId, int kidLinkCode) throws KidsbankException {
		purgeKidLinks();
		
		KidLink kidLink = kidLinkRepository.findOne(kidLinkCode);
		if (kidLink == null) {
			throw new KidsbankException("Unknown kid link code");
		}
		kidLinkRepository.delete(kidLink);
		
		Parent parent = parentsRepository.findOne(parentId);
		if (parent == null) {
			throw new KidsbankException("Unable to locate parent");
		}

		if (parent.getAccounts() != null) {
			for (String accountId : kidLink.getAccounts()) {
				if (!parent.getAccounts().contains(accountId)) {
					parent.getAccounts().add(accountId);
				}
			}
		}
		else {
			parent.setAccounts(kidLink.getAccounts());
		}
		
		parentsRepository.save(parent);
	}

	public int getMaxKidLinkAge() {
		return maxKidLinkAge;
	}

	public void setMaxKidLinkAge(int maxKidLinkAge) {
		this.maxKidLinkAge = maxKidLinkAge;
	}
}
