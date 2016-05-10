package me.itzg.kidsbank.services;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import me.itzg.kidsbank.KidsbankException;
import me.itzg.kidsbank.ParentUserDetails;
import me.itzg.kidsbank.domain.Parent;
import me.itzg.kidsbank.repositories.ParentsRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

@Service("parentUserDetailsService")
public class ParentUserDetailsService implements
		AuthenticationUserDetailsService<OpenIDAuthenticationToken> {
	private static final Logger logger = LoggerFactory.getLogger(ParentUserDetailsService.class);
	
	private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = 
			AuthorityUtils.createAuthorityList("parent");

	@Autowired
	ParentsRepository parentsRepository;

	public UserDetails loadUserDetails(OpenIDAuthenticationToken token)
			throws UsernameNotFoundException {
        String id = token.getIdentityUrl();
        logger.debug("Loading OpenID user details for {}",id);

        Parent dbUser = parentsRepository.findByOpenId(id);
        if (dbUser == null) {
        	dbUser = new Parent(id, new Date());
        	logger.debug("Saved {}", dbUser);
        }
        else {
        	logger.debug("Found existing user {}", dbUser);
        	dbUser.setLastAccessed(new Date());
        }
        parentsRepository.save(dbUser);
        
        return new ParentUserDetails(dbUser, DEFAULT_AUTHORITIES);
	}

	public Parent extractValidateParentOfAccount(Principal user, String accountId)
			throws KidsbankException {
		Parent parent = ParentUserDetails.extractFromPrincipal(user);

		parent = parentsRepository.findOne(parent.getOpenId());

		if (!parent.getAccounts().contains(accountId)) {
			throw new KidsbankException("Account "+accountId+" of "+parent+" does not exist");
		}

		return parent;
	}
	
	public void setNickname(Parent parentRef, String nickname) {
		parentRef.setNickname(nickname);
		Parent liveParent = parentsRepository.findOne(parentRef.getOpenId());
		liveParent.setNickname(nickname);
		parentsRepository.save(liveParent);
	}

	public Parent loadLive(Parent parent) {
		return parentsRepository.findOne(parent.getOpenId());
	}
}
