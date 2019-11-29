package com.canimesh.revolut.assignment.eventstore;

import java.util.List;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.Event;

import lombok.Value;

@Value
public class EventStream {
	private final UUID aggregateId;
	private final List<Event> events;
	public boolean isEmpty() {
        return events.isEmpty();
    }
}
