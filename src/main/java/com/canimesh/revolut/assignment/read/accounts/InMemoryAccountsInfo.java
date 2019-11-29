package com.canimesh.revolut.assignment.read.accounts;

import java.util.Optional;

import com.canimesh.revolut.assignment.domain.account.repo.AccountDataStore;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class InMemoryAccountsInfo implements AccountsInfoRetriever {

    private final AccountDataStore accountDataStore;


    @Override
    public Optional<AccountInfo> get(String accountId) {
        return Optional.ofNullable(accountDataStore.get(accountId));
    }
}
