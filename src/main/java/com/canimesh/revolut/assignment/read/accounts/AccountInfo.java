package com.canimesh.revolut.assignment.read.accounts;

import lombok.Value;

@Value
public class AccountInfo {

    private String accountId;
    private String owner;
    private long amount;
    

}
