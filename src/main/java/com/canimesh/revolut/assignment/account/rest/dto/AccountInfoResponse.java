package com.canimesh.revolut.assignment.account.rest.dto;

import lombok.Data;

@Data
public class AccountInfoResponse {

    private String accountId;
    private String owner;
    private long amount;
    
}
