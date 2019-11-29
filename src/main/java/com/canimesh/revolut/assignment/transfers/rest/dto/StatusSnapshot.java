package com.canimesh.revolut.assignment.transfers.rest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class StatusSnapshot {

	private final String time;
	private final String status;
}
