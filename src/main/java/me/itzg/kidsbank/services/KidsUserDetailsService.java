package me.itzg.kidsbank.services;

import java.util.List;

import me.itzg.kidsbank.KidUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KidsUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory
			.getLogger(KidsUserDetailsService.class);

	private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_PARENT");

	public UserDetails loadUserByUsername(String user)
			throws UsernameNotFoundException {
		logger.warn("Loading user with DEBUG ALGO {}", user);
		return new KidUserDetails(user, "", DEFAULT_AUTHORITIES);
	}

}
