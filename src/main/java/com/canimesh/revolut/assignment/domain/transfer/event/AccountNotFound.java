package com.canimesh.revolut.assignment.domain.transfer.event;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferState;
import com.canimesh.revolut.assignment.domain.transfer.TransferDetails;

import lombok.Getter;

@Getter
public class AccountNotFound extends MoneyTransferEvent {

    private final UUID notFoundAccountId;

    public AccountNotFound(UUID transferId, TransferDetails details,
                           UUID notFoundAccountId) {
        super(transferId, details);
        this.notFoundAccountId = notFoundAccountId;
    }

	@Override
	public MoneyTransferState state() {
		return MoneyTransferState.FAILED;
	}

}
