package com.canimesh.revolut.assignment.eventstore;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransfer;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferRepository;
import com.canimesh.revolut.assignment.domain.transfer.command.CreateMoneyTransfer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventSourcesMoneyTransferRepository implements MoneyTransferRepository {

	private final EventStore eventStore;


	@Override
	public MoneyTransfer save(MoneyTransfer transfer) {
		eventStore.store(transfer.getId(), transfer.getEvents());
		transfer.commit();
		return transfer;
	}

	@Override
	public Optional<MoneyTransfer> findById(UUID id) {
		EventStream eventStream = eventStore.load(id);
		if (eventStream.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(MoneyTransfer.from(eventStream.getEvents(), id));
	}

	@Override
	public MoneyTransfer create(CreateMoneyTransfer createMoneyTransfer) {
		MoneyTransfer transfer = new MoneyTransfer();
		transfer = transfer.create(createMoneyTransfer);
		eventStore.store(transfer.getId(), transfer.getEvents());
		transfer.commit();
		return transfer;
	}
}
