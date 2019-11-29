package com.canimesh.revolut.assignment.eventstore;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.account.Account;
import com.canimesh.revolut.assignment.domain.account.AccountRepository;
import com.canimesh.revolut.assignment.domain.account.command.CreateAccount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventSourcesAccountRepository implements AccountRepository {

	private final EventStore eventStore;

	@Override
	public Account save(Account account) {
		eventStore.store(account.getId(), account.getEvents());
		account.commit();
		return account;
	}

	@Override
	public Optional<Account> findById(UUID id) {
		EventStream eventStream = eventStore.load(id);
		if (eventStream.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(Account.from(eventStream.getEvents(), id));
	}

	@Override
	public Account createAccount(CreateAccount openAccount) {
		Account account = new Account();
		account = account.create(openAccount);
		eventStore.store(account.getId(), account.getEvents());
		account.commit();
		return account;
	}
}
