package com.canimesh.revolut.assignment.domain.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Aggregate {

    protected UUID id;
    protected Version version;
    protected List<Event> events;

    protected Aggregate() {
    	id = UUID.randomUUID();
    	events = new ArrayList<>();
    }
    protected Aggregate setVersion(Version version) {
        this.version = version;
        return this;
    }
    
    protected void setId(UUID id) {
    	this.id = id;
    }
    
    public void commit() {
        events.clear();
    }

    protected void addEvent(Event event) {
		this.events.add(event);
	}
}
