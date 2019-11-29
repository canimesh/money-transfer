package com.canimesh.revolut.assignment.domain.transfer.command;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RecordNotFoundAccount {

    private final UUID transferId;
    private final UUID notFoundAccountId;

    public RecordNotFoundAccount(UUID transferId,
                                 UUID notFoundAccountId) {
        this.transferId = transferId;
        this.notFoundAccountId = notFoundAccountId;
    }
}
