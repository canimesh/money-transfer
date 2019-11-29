package com.canimesh.revolut.assignment.domain.transfer.vo;

import java.time.Instant;

import com.canimesh.revolut.assignment.domain.common.State;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class StateChange {

	private final Instant instant;
	private final State state;
}
