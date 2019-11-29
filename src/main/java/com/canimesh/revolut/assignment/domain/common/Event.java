package com.canimesh.revolut.assignment.domain.common;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Event {

    private final Instant timestamp;
    private final UUID aggregateId;
    
    public Event(UUID aggregateId) {
        this.timestamp = Instant.now();
        this.aggregateId = aggregateId;
    }
    
    public abstract State state();
    
    public abstract EventType type();
}
