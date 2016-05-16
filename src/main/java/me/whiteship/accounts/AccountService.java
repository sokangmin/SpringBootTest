package me.whiteship.accounts;


import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class AccountService {
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;

	public Account createAccount(AccountDto.Create dto) {
		/*Account account = new Account();
		account.setUsername(dto.getUsername());
		account.setPassword(dto.getPassword());*/
		
		/*Account account = new Account();
		BeanUtils.copyProperties(dto, account);*/
		
		Account account = modelMapper.map(dto, Account.class);
		
		// 유효한 username인지 판단
		String username = dto.getUsername();
		if(repository.findByUsername(username) != null) {
			log.error("user duplicated exception. {}", username);
			throw new UserDuplicatedException(username);
		}
		
		// TODO password 해싱
		
		Date now = new Date();
		account.setJoined(now);
		account.setUpdated(now);
		
		return repository.save(account);
	}
	
	public Account updateAccount(Long id, AccountDto.Update updateDto) {
		Account account = getAccount(id);
		account.setPassword(updateDto.getPassword());
		account.setFullName(updateDto.getFullName());
		
		return repository.save(account);
	}

	public Account getAccount(Long id) {
		Account account = repository.findOne(id);
		if (account == null) {
			throw new AccountNotFoundException(id);
		}
		
		return account;
	}
}
