package com.canimesh.revolut.assignment.domain.transfer;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransferDetails {

    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final Money money;
}
