package com.canimesh.revolut.assignment.account.rest;

import java.util.Optional;

import com.canimesh.revolut.assignment.account.rest.dto.AccountInfoResponse;
import com.canimesh.revolut.assignment.account.rest.dto.CreateAccountRequest;
import com.canimesh.revolut.assignment.bound.context.AccountsContext;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;
import com.canimesh.revolut.assignment.read.accounts.AccountInfo;
import com.canimesh.revolut.assignment.read.accounts.AccountsInfoRetriever;

import lombok.RequiredArgsConstructor;
import spark.utils.StringUtils;

@RequiredArgsConstructor
public class AccountsRestController {

	private final AccountsInfoRetriever accountsInfoRetriever;
	private final AccountsContext accountsContext;


	public AccountInfoResponse get(String accountId) {
		Optional<AccountInfo> respOpt = accountsInfoRetriever.get(accountId);
		return convert(respOpt.orElseThrow(() -> {
			String message = String.format("Account with id %s not found.", accountId);
			return new ApplicationRuntimeException(404, message);
		}));
	}

	private AccountInfoResponse convert(AccountInfo accountInfo) {
		AccountInfoResponse accountInfoResponse = new AccountInfoResponse();
		accountInfoResponse.setAccountId(accountInfo.getAccountId());
		accountInfoResponse.setAmount(accountInfo.getAmount());
		accountInfoResponse.setOwner(accountInfo.getOwner());
		return accountInfoResponse;
	}

	public String create(CreateAccountRequest request) {
		if (request == null || request.getAmount()<0 || StringUtils.isBlank(request.getOwner())) {
            throw new ApplicationRuntimeException(400, "Invalid request");
        }
		return accountsContext.createAccount(request.getOwner(),new Money(request.getAmount()));

	}

}
