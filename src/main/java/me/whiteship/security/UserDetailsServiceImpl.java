package me.whiteship.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.whiteship.accounts.Account;
import me.whiteship.accounts.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new UserDetailsImpl(account);
	}

}
