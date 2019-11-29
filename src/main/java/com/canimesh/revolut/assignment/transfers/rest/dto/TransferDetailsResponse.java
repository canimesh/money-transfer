package com.canimesh.revolut.assignment.transfers.rest.dto;

import java.util.List;

import lombok.Data;

@Data
public class TransferDetailsResponse {
	private String id;
    private String fromAccount;
    private String toAccount;
    private long amount;
    private List<StatusSnapshot> statusChanges;
}
