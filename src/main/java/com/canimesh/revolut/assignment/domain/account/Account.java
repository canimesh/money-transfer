package com.canimesh.revolut.assignment.domain.account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.canimesh.revolut.assignment.domain.account.command.CreateAccount;
import com.canimesh.revolut.assignment.domain.account.command.CreditAccount;
import com.canimesh.revolut.assignment.domain.account.command.DebitAccount;
import com.canimesh.revolut.assignment.domain.account.event.AccountCredited;
import com.canimesh.revolut.assignment.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.canimesh.revolut.assignment.domain.account.event.AccountDebited;
import com.canimesh.revolut.assignment.domain.account.event.AccountEvent;
import com.canimesh.revolut.assignment.domain.account.event.AccountOpened;
import com.canimesh.revolut.assignment.domain.common.Aggregate;
import com.canimesh.revolut.assignment.domain.common.Event;
import com.canimesh.revolut.assignment.domain.common.EventType;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;
import com.canimesh.revolut.assignment.domain.transfer.vo.StateChange;

import lombok.ToString;

@ToString
public class Account extends Aggregate {

	private Money balance;
	private List<StateChange> stateChanges = new ArrayList<>();
	
	public static Account from(List<Event> history, UUID aggregateId) {
		List<StateChange> listOfStateChanges = history.stream()
				.filter(f -> f.type().equals(EventType.ACCOUNT))
				.sorted((a, b) -> {
					return a.getTimestamp().compareTo(b.getTimestamp());
				}).map(e -> {
					return new StateChange(e.getTimestamp(), e.state());
				}).collect(Collectors.toList());
		Account account = new Account();
		account.setId(aggregateId);
		account.stateChanges = listOfStateChanges;
		account.balance = ((AccountEvent)history.get(0)).getMoney();
		return account;
	}
	

	public Account create(CreateAccount createAccount) {
		Money initialBalance = createAccount.getAccountDetails().getMoney();
		if (isNegative(initialBalance)) {
			throw new IllegalArgumentException("Can't open an account with negative initial balance");
		}
		addEvent(new AccountOpened(this.id, createAccount.getAccountDetails()));
		return this;
	}

	private boolean isNegative(Money initialBalance) {
		return initialBalance.getAmount() < 0;
	}

	public Account credit(CreditAccount credit) {
		Money amount = credit.getAmount();
		if (isNegative(amount)) {
			throw new IllegalArgumentException("Can't credit negative money amount");
		}
		addEvent(new AccountCredited(id, amount, credit.getTransferId()));
		return this;
	}

	public Account debit(DebitAccount debit) {
		Money amount = debit.getAmount();
		UUID transferId = debit.getTransferId();
		if (isNegative(amount)) {
			throw new IllegalArgumentException("Can't debit negative money amount");
		}
		if (balance.getAmount()<amount.getAmount()) {
			addEvent(new AccountDebitFailedDueToInsufficientFunds(id, amount, transferId));
		} else {
			addEvent(new AccountDebited(id, amount, transferId));
		}
		return this;
	}


//	private Account debitFailed(AccountDebitFailedDueToInsufficientFunds debitFailed) {
//		return this;
//	}
//
//	private Account debited(AccountDebited debited) {
//		this.balance = new Money(this.balance.getAmount()-debited.getMoney().getAmount());
//		return this;
//	}
//
//	private Account credited(AccountCredited credited) {
//		this.balance = new Money(this.balance.getAmount()+credited.getMoney().getAmount());
//		return this;
//	}
//
//	private Account opened(AccountOpened opened) {
//		balance = opened.getMoney();
//		id = opened.getAggregateId();
//		return this;
//	}


}
