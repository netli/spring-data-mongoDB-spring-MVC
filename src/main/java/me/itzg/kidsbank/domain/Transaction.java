package me.itzg.kidsbank.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Transaction {
	@Id
	private String id;
	
	private Date when;
	
	private double amount;
	
	private String description;
	
	private String accountId;
	
	private String parentId;
	
	public Transaction() {
	}
	
	@Override
	public String toString() {
		return Transaction.class.getSimpleName()+":[id="+id
				+", when="+when
				+", amount="+amount
				+", description="+description
				+"]";
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAccount(Account account) {
		this.accountId = account.getId();
	}

	public void setParent(Parent parent) {
		this.parentId = parent.getOpenId();
	}

	public String getId() {
		return id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}
