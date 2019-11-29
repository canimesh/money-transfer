package com.canimesh.revolut.assignment.eventstore;

import java.util.List;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.Event;

public interface EventStore {

	void store(UUID id, List<Event> events);

	EventStream load(UUID id);
}
