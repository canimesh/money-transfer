package com.canimesh.revolut.assignment.eventstore;

import java.util.List;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.common.Event;
import com.canimesh.revolut.assignment.eventbus.EventBus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryEventStore implements EventStore {

	private final EventStreamsStore eventStreamsStore;
	private final EventBus eventBus;
	private final TransactionManager txManager;


	@Override
	public void store(UUID id, List<Event> events) {
		txManager.doInTx(id, () -> {
			eventStreamsStore.get(id).getEvents().addAll(events);
		});
		publishEvents(events);
	}

	@Override
	public EventStream load(UUID id) {
		return eventStreamsStore.get(id);
	}

	private void publishEvents(List<Event> events) {
		events.forEach(eventBus::post);
	}

}
