package me.itzg.kidsbank.domain;

import java.io.Serializable;

import me.itzg.kidsbank.shared.LocaleSpec;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String name;
	
	private LocaleSpec locale;
	
	private AllowanceSpec allowance;
	
	public Account() {
	}
	
	@Override
	public String toString() {
		return Account.class.getSimpleName()+"[id="+id
				+", name="+name
				+"]";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocaleSpec getLocale() {
		return locale;
	}

	public void setLocale(LocaleSpec locale) {
		this.locale = locale;
	}

	public String getId() {
		return id;
	}

	public AllowanceSpec getAllowance() {
		return allowance;
	}

	public void setAllowance(AllowanceSpec allowance) {
		this.allowance = allowance;
	}
}
