package com.canimesh.revolut.assignment.domain.account.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.State;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Getter;

@Getter
public class AccountDebitFailedDueToInsufficientFunds extends AccountEvent {

    private final UUID transferId;

    public AccountDebitFailedDueToInsufficientFunds(UUID accountId, Money money,
                                                    UUID transferId) {
        super(accountId);
        this.money = money;
        this.transferId = transferId;
    }

	@Override
	public State state() {
		return AccountStateChange.FAILED;
	}

}
