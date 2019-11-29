package com.canimesh.revolut.assignment.domain.transfer.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.Event;
import com.canimesh.revolut.assignment.domain.common.EventType;
import com.canimesh.revolut.assignment.domain.transfer.TransferDetails;

import lombok.Getter;

@Getter
public abstract class MoneyTransferEvent extends Event {

    private final TransferDetails details;

    public MoneyTransferEvent(UUID transferId, TransferDetails details) {
        super(transferId);
        this.details = details;
    }
    
    public EventType type() {
    	return EventType.MONEY_TRANSFER;
    }

}
