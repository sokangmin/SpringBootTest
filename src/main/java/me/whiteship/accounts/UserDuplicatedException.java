package me.whiteship.accounts;

import lombok.Getter;

@Getter
public class UserDuplicatedException extends RuntimeException {

	private String username;
	
	public UserDuplicatedException(String username) {
		this.username = username;
	}

}
