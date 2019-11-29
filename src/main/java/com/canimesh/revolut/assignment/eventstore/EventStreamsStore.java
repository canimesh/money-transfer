package com.canimesh.revolut.assignment.eventstore;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventStreamsStore {

	private final Map<UUID,EventStream> eventStreams = new ConcurrentHashMap<>();
	
	public void save(UUID id, EventStream eventStream) {
		eventStreams.put(id, eventStream);
		
	}

	public EventStream get(UUID id) {
		eventStreams.putIfAbsent(id, new EventStream(id, new ArrayList<>()));
		return eventStreams.get(id);
		
	}

	
	
}
