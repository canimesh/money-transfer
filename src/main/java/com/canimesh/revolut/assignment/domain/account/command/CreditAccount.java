package com.canimesh.revolut.assignment.domain.account.command;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class CreditAccount {

    private final Money amount;
    private final UUID transferId;

}
