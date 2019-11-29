package com.canimesh.revolut.assignment.bound.context;


import com.canimesh.revolut.assignment.domain.account.Account;
import com.canimesh.revolut.assignment.domain.account.AccountService;
import com.canimesh.revolut.assignment.domain.account.vo.AccountDetails;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountsContext {

    private final AccountService accountService;


    public String createAccount(String owner, Money initialBalance) {
        Account opened = accountService.createAccount(new AccountDetails(initialBalance, owner));
        return opened.getId().toString();
    }
}
