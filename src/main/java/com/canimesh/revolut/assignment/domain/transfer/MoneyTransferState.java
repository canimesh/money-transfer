package com.canimesh.revolut.assignment.domain.transfer;

import com.canimesh.revolut.assignment.domain.common.State;

public enum MoneyTransferState implements State{
    INITIAL, DEBITED, CREDITED, FAILED
}
