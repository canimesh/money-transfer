package com.canimesh.revolut.assignment.domain.account.command;

import com.canimesh.revolut.assignment.domain.account.vo.AccountDetails;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
public class CreateAccount {

   private final AccountDetails accountDetails;

}
