package com.canimesh.revolut.assignment.domain.account.vo;

import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.Value;

@Value
public class AccountDetails {

	private Money money;
	private String owner;
}
