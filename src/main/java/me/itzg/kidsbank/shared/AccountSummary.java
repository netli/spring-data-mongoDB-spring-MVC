package me.itzg.kidsbank.shared;

import org.bson.types.ObjectId;


public class AccountSummary {
	private String id;
	
	private String name;
	
	private String balance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setId(ObjectId id) {
		this.id = id.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String amount) {
		this.balance = amount;
	}
}
