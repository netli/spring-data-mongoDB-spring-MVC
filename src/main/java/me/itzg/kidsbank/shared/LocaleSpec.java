package me.itzg.kidsbank.shared;

import java.io.Serializable;
import java.util.Locale;

public class LocaleSpec implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String language;
	
	private String country;
	
	public LocaleSpec() {
	}
	
	public LocaleSpec(Locale locale) {
		this.language = locale.getLanguage();
		this.country = locale.getCountry();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof LocaleSpec)) return false;
		
		LocaleSpec rhs = (LocaleSpec) obj;
		return rhs.language.equals(this.language) 
				&& rhs.country.equals(this.country);
	}
	
	public Locale toLocale() {
		return new Locale(language, country);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public static Locale parseLocaleCode(String localeCode) {
		String[] parts = localeCode.split("_");
		switch (parts.length) {
		case 1:
			return new Locale(parts[0]);
		case 2:
			return new Locale(parts[0], parts[1]);
		case 3:
			return new Locale(parts[0], parts[1], parts[2]);
		}
		return null;
	}
	
}
