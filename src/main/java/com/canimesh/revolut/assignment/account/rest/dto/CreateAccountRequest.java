package com.canimesh.revolut.assignment.account.rest.dto;

import lombok.Value;

@Value
public class CreateAccountRequest {

	private long amount;

	private String owner;
}
