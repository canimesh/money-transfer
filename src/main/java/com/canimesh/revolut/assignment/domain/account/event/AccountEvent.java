package com.canimesh.revolut.assignment.domain.account.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.Event;
import com.canimesh.revolut.assignment.domain.common.EventType;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class AccountEvent extends Event {

	protected Money money;
    public AccountEvent(UUID accountId) {
        super(accountId);
    }
    
    public EventType type() {
    	return EventType.ACCOUNT;
    }
}
