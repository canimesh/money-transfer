package com.canimesh.revolut.assignment.domain.account.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.State;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Getter;

@Getter
public class AccountCredited extends AccountEvent {

    private final UUID transferId;

    public AccountCredited(UUID accountId, Money amount, UUID transferId) {
        super(accountId);
        this.money = amount;
        this.transferId = transferId;
    }

	@Override
	public State state() {
		return AccountStateChange.CREDITED;
	}

}
