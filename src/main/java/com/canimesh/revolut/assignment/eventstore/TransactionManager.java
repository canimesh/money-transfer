package com.canimesh.revolut.assignment.eventstore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionManager {

    private ConcurrentMap<UUID, ReentrantLock> idToLock;

    public TransactionManager() {
        this.idToLock = new ConcurrentHashMap<>();
    }

    public void doInTx(UUID aggregateId, Runnable action) {
        Lock aggregateLock = getOrCreateLock(aggregateId);
        try {
            aggregateLock.lock();
            action.run();
        } finally {
            aggregateLock.unlock();
        }
    }

    private Lock getOrCreateLock(UUID aggregateId) {
        Lock aggregateLock = idToLock.get(aggregateId);
        if (aggregateLock == null) {
            idToLock.putIfAbsent(aggregateId, new ReentrantLock());
            aggregateLock = idToLock.get(aggregateId);
        }
        return aggregateLock;
    }
}
