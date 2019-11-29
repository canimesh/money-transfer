package com.canimesh.revolut.assignment.domain.transfer.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferState;
import com.canimesh.revolut.assignment.domain.transfer.TransferDetails;

public class FailedDebitRecorded extends MoneyTransferEvent {

    public FailedDebitRecorded(UUID transferId, TransferDetails transferDetails) {
        super(transferId, transferDetails);
    }

	@Override
	public MoneyTransferState state() {
		return MoneyTransferState.FAILED;
	}

}
