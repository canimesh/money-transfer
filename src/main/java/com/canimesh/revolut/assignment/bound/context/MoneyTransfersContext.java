package com.canimesh.revolut.assignment.bound.context;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferService;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoneyTransfersContext {

	private final MoneyTransferService moneyTransferService;


	private String transferMoney(String fromAccountId, String toAccountId, Money amount) {
		return moneyTransferService
				.transferMoney(UUID.fromString(fromAccountId), UUID.fromString(toAccountId), amount).getId()
				.toString();
	}

	public String transferMoney(String fromAccountId, String toAccountId, long amount) {
		return transferMoney(fromAccountId, toAccountId,
				new Money(amount));
	}

}
