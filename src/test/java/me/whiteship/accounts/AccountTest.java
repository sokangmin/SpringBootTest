package me.whiteship.accounts;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class AccountTest {
	
	@Test
	public void getterSetter() {
		Account account = new Account();
		account.setUsername("username");
		account.setPassword("password");
		
		assertThat(account.getUsername(), is("username"));
	}
}
