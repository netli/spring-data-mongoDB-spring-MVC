package me.itzg.kidsbank.shared;

public class CountryResponse {
	private String displayCountry;
	
	private String locale;
	
	private boolean userLocale;

	public String getDisplayCountry() {
		return displayCountry;
	}

	public CountryResponse(String displayCountry, String locale, boolean userLocale) {
		super();
		this.displayCountry = displayCountry;
		this.locale = locale;
		this.userLocale = userLocale;
	}

	public void setDisplayCountry(String displayCountry) {
		this.displayCountry = displayCountry;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isUserLocale() {
		return userLocale;
	}

	public void setUserLocale(boolean matchesUser) {
		this.userLocale = matchesUser;
	}
}
