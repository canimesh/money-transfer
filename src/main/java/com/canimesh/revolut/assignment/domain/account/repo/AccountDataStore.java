package com.canimesh.revolut.assignment.domain.account.repo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.canimesh.revolut.assignment.read.accounts.AccountInfo;

public class AccountDataStore {

	private final Map<String, AccountInfo> database;
	
	public AccountDataStore() {
		database = new ConcurrentHashMap<>();
	}

	public void save(AccountInfo accountInfo) {
		database.put(accountInfo.getAccountId(), accountInfo);
	}

	public AccountInfo get(String accountId) {
		return database.get(accountId);
	}
	
	
}
