package com.canimesh.revolut.assignment.domain.transfer.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferState;
import com.canimesh.revolut.assignment.domain.transfer.TransferDetails;

import lombok.Getter;

@Getter
public class MoneyTransferCreated extends MoneyTransferEvent {

	public MoneyTransferCreated(UUID transferId, TransferDetails details) {
		super(transferId, details);
	}

	@Override
	public MoneyTransferState state() {
		return MoneyTransferState.INITIAL;
	}

}
