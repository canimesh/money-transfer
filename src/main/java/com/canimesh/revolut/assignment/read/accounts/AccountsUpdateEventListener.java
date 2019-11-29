package com.canimesh.revolut.assignment.read.accounts;

import java.util.Optional;

import com.canimesh.revolut.assignment.domain.account.event.AccountCredited;
import com.canimesh.revolut.assignment.domain.account.event.AccountDebited;
import com.canimesh.revolut.assignment.domain.account.event.AccountOpened;
import com.canimesh.revolut.assignment.domain.account.repo.AccountDataStore;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;
import com.canimesh.revolut.assignment.eventbus.EventBus;

public class AccountsUpdateEventListener {

	private final AccountDataStore accountDataStore;

	public AccountsUpdateEventListener(EventBus eventBus,
			AccountDataStore accountDataStore) {
		this.accountDataStore = accountDataStore;
		setUpSubscribers(eventBus);
	}

	private void setUpSubscribers(EventBus eventBus) {
		eventBus.subscribe(AccountCredited.class, (event)->{this.credited((AccountCredited)event);});
		eventBus.subscribe(AccountDebited.class, (event)->{this.debited((AccountDebited)event);});
		eventBus.subscribe(AccountOpened.class, (event)->{this.opened((AccountOpened)event);});
	}

	public void credited(AccountCredited credited) {
		String accountId = credited.getAggregateId().toString();
		Optional<AccountInfo> accountInfoOpt = find(accountId);
		accountInfoOpt.ifPresent(old -> {
			addMoney(old, credited.getMoney());
		});
	}

	public void debited(AccountDebited debited) {
		String accountId = debited.getAggregateId().toString();
		Optional<AccountInfo> accountInfoOpt = find(accountId);
		accountInfoOpt.ifPresent(old -> {
			subtractMoney(old, debited.getMoney());
		});
	}

	public void opened(AccountOpened opened) {
		String idAsString = opened.getAggregateId().toString();

		AccountInfo accountInfo = new AccountInfo(idAsString,opened.getOwner(),
				opened.getMoney().getAmount());
		accountDataStore.save(accountInfo);
	}

	private void addMoney(AccountInfo account, Money amount) {
		AccountInfo accountInfo = new AccountInfo(account.getAccountId(),account.getOwner(),
				account.getAmount()+amount.getAmount());
		accountDataStore.save(accountInfo);
	}

	private void subtractMoney(AccountInfo account, Money amount) {
		AccountInfo accountInfo = new AccountInfo(account.getAccountId(),account.getOwner(),
				account.getAmount()-amount.getAmount());
		accountDataStore.save(accountInfo);
	}

	private Optional<AccountInfo> find(String accountId) {
		return Optional.ofNullable(accountDataStore.get(accountId));
	}

}
