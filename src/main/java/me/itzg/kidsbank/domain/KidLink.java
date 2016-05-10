package me.itzg.kidsbank.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class KidLink {
	@Id
	private int code;
	
	private Date created;

	private List<String> accounts;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
}
