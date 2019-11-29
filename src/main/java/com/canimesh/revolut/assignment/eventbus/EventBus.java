package com.canimesh.revolut.assignment.eventbus;

import java.util.function.Consumer;

import com.canimesh.revolut.assignment.domain.common.Event;

public interface EventBus {

	void post(Event event);
	
	void subscribe(Class<?> type, Consumer<Event> consumer);

}