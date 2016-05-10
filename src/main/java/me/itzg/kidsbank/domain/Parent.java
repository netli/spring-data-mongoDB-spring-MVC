package me.itzg.kidsbank.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import me.itzg.kidsbank.shared.LocaleSpec;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Parent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String openId;
	
	private Date created;
	
	private Date lastAccessed;
	
	private List<String/*id*/> accounts;
	
	private String nickname;
	
	private LocaleSpec defaultLocale;
	
	public Parent() {
	}

	public Parent(String openId, Date created) {
		super();
		this.openId = openId;
		this.created = created;
		this.lastAccessed = created;
	}
	
	@Override
	public String toString() {
		return Parent.class.getSimpleName()+":[openId="+openId+"]";
	}

	public String getOpenId() {
		return openId;
	}
	
	public Date getCreated() {
		return created;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public LocaleSpec getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(LocaleSpec defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
}