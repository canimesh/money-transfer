package com.canimesh.revolut.assignment.domain.transfer.command;

import com.canimesh.revolut.assignment.domain.transfer.TransferDetails;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class CreateMoneyTransfer {

    private final TransferDetails details;

}
