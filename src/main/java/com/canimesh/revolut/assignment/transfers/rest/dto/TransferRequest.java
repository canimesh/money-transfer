package com.canimesh.revolut.assignment.transfers.rest.dto;

import lombok.Data;

@Data
public class TransferRequest {

    private final String fromAccount;
    private final String toAccount;
    private final long amount;
}
