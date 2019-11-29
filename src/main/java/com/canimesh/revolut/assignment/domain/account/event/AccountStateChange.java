package com.canimesh.revolut.assignment.domain.account.event;

import com.canimesh.revolut.assignment.domain.common.State;

public enum AccountStateChange implements State {
	CREATED, DEBITED, FAILED, CREDITED;
}
