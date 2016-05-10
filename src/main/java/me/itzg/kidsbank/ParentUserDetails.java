package me.itzg.kidsbank;

import java.security.Principal;
import java.util.Collection;

import me.itzg.kidsbank.domain.Parent;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class ParentUserDetails extends User {
	private static final long serialVersionUID = 1L;
	private Parent dbUser;

	public ParentUserDetails(Parent dbUser,
			Collection<? extends GrantedAuthority> authorities) {
		super(dbUser.getOpenId(), "UNUSED", authorities);
		this.dbUser = dbUser;
	}
	
	public Parent getDbUser() {
		return dbUser;
	}

	/**
	 * 
	 * @param user
	 * @return the parent object associated with the given user principal; however, be aware
	 * 	that the object may be "stale" other than the id field
	 */
	public static Parent extractFromPrincipal(Principal user) {
		Authentication authenticatedUser = (Authentication) user;
		ParentUserDetails battsUserDetails = (ParentUserDetails) authenticatedUser.getPrincipal();
		return battsUserDetails.getDbUser();
	}

}
