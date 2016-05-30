package me.whiteship.accounts;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.whiteship.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class AccountControllerTest {
	
	@Autowired
	WebApplicationContext wac;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	AccountService service;
	
	MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecuretyFilterChain;
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(springSecuretyFilterChain)
				.build();
	}

	@Test
	public void createAccount() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		
		
		ResultActions result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isCreated());
		
		// TODO JSON Path
		// {"id":1,"username":"username","fullName":null,"joined":1462943920905,"updated":1462943920905}
		result.andExpect(jsonPath("$.username", is("username")));
	}
	
	@Test
	public void createAccount_BadRequest() throws Exception {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUsername("  ");
		createDto.setPassword("1234");
		
		ResultActions result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.code", is("bad.request")));
	}
	
	@Test
	public void createAccount_DuplicatedUsername() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		
		ResultActions result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isCreated());
		
		result = mockMvc.perform(post("/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDto)));
		
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("$.code", is("duplicated.username.exception")));
	}

	@Test
	public void getAccounts() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		service.createAccount(createDto);
		
		ResultActions result = mockMvc.perform(get("/accounts"));
		
		
		// {"content":
		// [{"id":1,"username":"username","fullName":null,"joined":1463030485681,"updated":1463030485681}],
		// "last":true,
		// "totalElements":1,
		// "totalPages":1,
		// "size":20,
		// "number":0,
		// "sort":null,
		// "numberOfElements":1,
		// "first":true}
		result.andDo(print());
		result.andExpect(status().isOk());
	}

	private AccountDto.Create accountCreateFixture() {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUsername("username");
		createDto.setPassword("password");
		return createDto;
	}
	
	@Test
	public void getAccount() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		Account account = service.createAccount(createDto);
		
		ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));
		
		result.andDo(print());
		result.andExpect(status().isOk());
	}
	
	@Test
	public void updateAccount() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		Account account = service.createAccount(createDto);
		
		AccountDto.Update updateDto = new AccountDto.Update();
		updateDto.setFullName("sokangmin");
		updateDto.setPassword("pass");
		
		ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)));
		
		result.andDo(print());
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.fullName", is("sokangmin")));
	}
	
	@Test
	public void deleteAccount() throws Exception {
		AccountDto.Create createDto = accountCreateFixture();
		Account account = service.createAccount(createDto);
		
		ResultActions result = mockMvc.perform(delete("/accounts/12345")
								.with(httpBasic(createDto.getUsername(), createDto.getPassword())));
		
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		
		//AccountDto.Create createDto = accountCreateFixture();
		
		result = mockMvc.perform(delete("/accounts/" + account.getId())
				.with(httpBasic(createDto.getUsername(), createDto.getPassword())));
		result.andDo(print());
		result.andExpect(status().isNoContent());
	}
}
