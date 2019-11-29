package com.canimesh.revolut.assignment.domain.account;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.account.command.CreateAccount;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(UUID id);

    Account createAccount(CreateAccount openAccount);

}
