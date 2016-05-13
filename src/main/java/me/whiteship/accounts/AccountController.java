package me.whiteship.accounts;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import me.whiteship.commons.ErrorResponse;

@RestController
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping("/hello")
	public String hello() {

		return "Hello Spring Boot";
	}
	
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, 
										BindingResult result){
		if (result.hasErrors()) {
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setMessage("잘못된 요청입니다.");
			errorResponse.setCode("bad.request");
			// TODO BindingResult 안에 들어있는 에러 정보 사용하기
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		Account newAccount = service.createAccount(create);
		
		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class)
				, HttpStatus.CREATED);
	}
	
	// /accounts?page=0&size=20&sort=username&sort=joined,desc
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public ResponseEntity getAccounts(Pageable pageable) {
		Page<Account> page = repository.findAll(pageable);
		
		// TODO stream() vs parallelStream()
		List<AccountDto.Response> content = page.getContent().parallelStream()
				.map(account -> modelMapper.map(account, AccountDto.Response.class))
				.collect(Collectors.toList());
		
		PageImpl<AccountDto.Response> result = new PageImpl<>(content, pageable, page.getTotalElements());
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		
	}
	
	// TODO 예외 처리 네번째 방법 (콜백 비슷한거...)
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("[" + e.getUsername() + "] 중복된 username 입니다.");
		errorResponse.setCode("duplicated.username.exception");
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
}
