package me.itzg.kidsbank;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class KidUserDetails extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KidUserDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

}
