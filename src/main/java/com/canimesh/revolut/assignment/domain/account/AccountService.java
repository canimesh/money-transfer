package com.canimesh.revolut.assignment.domain.account;

import com.canimesh.revolut.assignment.domain.account.command.CreateAccount;
import com.canimesh.revolut.assignment.domain.account.vo.AccountDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public Account createAccount(AccountDetails accountDetails) {
        
        CreateAccount createAccountCommand = new CreateAccount(accountDetails);
        return accountRepository.createAccount(createAccountCommand);
    }

}
