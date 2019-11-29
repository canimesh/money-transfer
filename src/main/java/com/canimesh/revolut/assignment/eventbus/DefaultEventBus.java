package com.canimesh.revolut.assignment.eventbus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.canimesh.revolut.assignment.domain.common.Event;

public class DefaultEventBus implements EventBus {

	private  final Map<Class<?>, List<Consumer<Event>>> subscribers = new HashMap<>();

	@Override
	public void subscribe(Class<?> type, Consumer<Event> consumer) {
		subscribers.putIfAbsent(type, new ArrayList<>());
		subscribers.get(type).add(consumer);
	}

	@Override
	public void post(Event event) {
		subscribers.getOrDefault(event.getClass(), Collections.emptyList()).parallelStream().forEach(action -> action.accept(event.getClass().cast(event)));
	}

}
