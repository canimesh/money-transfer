package com.canimesh.revolut.assignment.account.rest;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.canimesh.revolut.assignment.account.rest.dto.AccountInfoResponse;
import com.canimesh.revolut.assignment.account.rest.dto.CreateAccountRequest;
import com.canimesh.revolut.assignment.bound.context.AccountsContext;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;
import com.canimesh.revolut.assignment.read.accounts.AccountInfo;
import com.canimesh.revolut.assignment.read.accounts.AccountsInfoRetriever;

public class AccountsRestControllerTest {

	private AccountsRestController accountsRestController;
	@Mock
	private AccountsInfoRetriever accountsInfoRetriever;
	@Mock
	private AccountsContext accountsContext;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		accountsRestController = new AccountsRestController(accountsInfoRetriever,accountsContext);
	}
	
	@Test(expected = ApplicationRuntimeException.class)
	public void test_get_error() {
		Mockito.when(accountsInfoRetriever.get("account_id")).thenReturn(Optional.empty());
		accountsRestController.get("account_id");
	}
	
	@Test
	public void test_get_success() {
		Mockito.when(accountsInfoRetriever.get("account_id")).thenReturn(Optional.of(new AccountInfo("accid", "owner", 500)));
		AccountInfoResponse accountInfoResponse = accountsRestController.get("account_id");
		assertEquals("accid", accountInfoResponse.getAccountId());
		assertEquals("owner", accountInfoResponse.getOwner());
		assertEquals(500, accountInfoResponse.getAmount());
	}
	
	@Test
	public void test_create_success() {
		
		Mockito.when(accountsContext.createAccount("owner", new Money(500))).thenReturn("some_id");
		String id = accountsRestController.create(new CreateAccountRequest(500, "owner"));
		assertEquals("some_id",id);
	}
	
	@Test(expected = ApplicationRuntimeException.class)
	public void test_create_error() {
		
		accountsRestController.create(new CreateAccountRequest(0, ""));
	}

}
