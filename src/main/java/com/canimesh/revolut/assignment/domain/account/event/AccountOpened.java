package com.canimesh.revolut.assignment.domain.account.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.account.vo.AccountDetails;
import com.canimesh.revolut.assignment.domain.common.State;

import lombok.Getter;


@Getter
public class AccountOpened extends AccountEvent {

    private final String owner;

    public AccountOpened(UUID accountId, AccountDetails accountDetails) {
        super(accountId);
        this.money = accountDetails.getMoney();
        this.owner = accountDetails.getOwner();
    }

	@Override
	public State state() {
		return AccountStateChange.CREATED;
	}

}
