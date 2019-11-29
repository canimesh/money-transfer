package com.canimesh.revolut.assignment.domain.transfer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.canimesh.revolut.assignment.domain.common.Aggregate;
import com.canimesh.revolut.assignment.domain.common.Event;
import com.canimesh.revolut.assignment.domain.common.EventType;
import com.canimesh.revolut.assignment.domain.common.State;
import com.canimesh.revolut.assignment.domain.transfer.command.CreateMoneyTransfer;
import com.canimesh.revolut.assignment.domain.transfer.command.RecordNotFoundAccount;
import com.canimesh.revolut.assignment.domain.transfer.event.AccountNotFound;
import com.canimesh.revolut.assignment.domain.transfer.event.CreditRecorded;
import com.canimesh.revolut.assignment.domain.transfer.event.DebitRecorded;
import com.canimesh.revolut.assignment.domain.transfer.event.FailedDebitRecorded;
import com.canimesh.revolut.assignment.domain.transfer.event.MoneyTransferCreated;
import com.canimesh.revolut.assignment.domain.transfer.event.MoneyTransferEvent;
import com.canimesh.revolut.assignment.domain.transfer.vo.StateChange;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MoneyTransfer extends Aggregate {

	private TransferDetails details;
	private List<StateChange> stateChanges = new ArrayList<>();

	public static MoneyTransfer from(List<Event> history, UUID aggregateId) {
		List<StateChange> listOfStateChanges = history.stream()
				.filter(f -> f.type().equals(EventType.MONEY_TRANSFER))
				.sorted((a, b) -> {
					return a.getTimestamp().compareTo(b.getTimestamp());
				}).map(e -> {
					return new StateChange(e.getTimestamp(), e.state());
				}).collect(Collectors.toList());
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setId(aggregateId);
		moneyTransfer.stateChanges = listOfStateChanges;
		moneyTransfer.details = ((MoneyTransferEvent)history.get(0)).getDetails();
		return moneyTransfer;
	}

	private void addState(State state) {
		stateChanges.add(new StateChange(Instant.now(), state));
	}

	public MoneyTransfer create(CreateMoneyTransfer create) {
		TransferDetails details = create.getDetails();
		if (invalidAmount(details)) {
			throw new ApplicationRuntimeException(400, "Invalid transfer amount");
		}
		if (sameAccount(details)) {
			throw new ApplicationRuntimeException(400, "Invalid input. Cannot transfer to self.");
		}
		addEvent(new MoneyTransferCreated(id, details));
		addState(MoneyTransferState.INITIAL);
		return this;
	}

	private boolean invalidAmount(TransferDetails details) {
		return details.getMoney().getAmount() <= 0;
	}

	public MoneyTransfer recordNotFoundAccount(RecordNotFoundAccount notFoundAccount) {
		addEvent(new AccountNotFound(id, details, notFoundAccount.getNotFoundAccountId()));
		addState(MoneyTransferState.FAILED);
		return this;
	}

	public MoneyTransfer recordDebit() {
		addEvent(new DebitRecorded(id, details));
		addState(MoneyTransferState.DEBITED);
		return this;
	}

	public MoneyTransfer recordCredit() {
		addEvent(new CreditRecorded(id, details));
		addState(MoneyTransferState.CREDITED);
		return this;
	}

	public MoneyTransfer recordFailedDebit() {
		addEvent(new FailedDebitRecorded(id, details));
		addState(MoneyTransferState.FAILED);
		return this;
	}

	private boolean sameAccount(TransferDetails transferDetails) {
		return transferDetails.getFromAccountId().equals(transferDetails.getToAccountId());
	}

//	private MoneyTransfer created(MoneyTransferCreated transferCreated) {
//		id = transferCreated.getAggregateId();
//		details = transferCreated.getDetails();
//		state = MoneyTransferState.INITIAL;
//		return this;
//	}
//
//	private MoneyTransfer debitRecorded(DebitRecorded debitRecorded) {
//		state = MoneyTransferState.DEBITED;
//		return this;
//	}
//
//	private MoneyTransfer creditRecorded(CreditRecorded creditRecorded) {
//		state = MoneyTransferState.CREDITED;
//		return this;
//	}
//
//	private MoneyTransfer failedDebitRecorded(FailedDebitRecorded failedDebitRecorded) {
//		state = MoneyTransferState.FAILED;
//		return this;
//	}
//
//	private MoneyTransfer accountNotFound(AccountNotFound accountNotFound) {
//		state = MoneyTransferState.FAILED;
//		return this;
//	}

//	private MoneyTransfer handle(MoneyTransferEvent event) {
//		return Match(event).of(Case($(instanceOf(MoneyTransferCreated.class)), this::created),
//				Case($(instanceOf(DebitRecorded.class)), this::debitRecorded),
//				Case($(instanceOf(FailedDebitRecorded.class)), this::failedDebitRecorded),
//				Case($(instanceOf(AccountNotFound.class)), this::accountNotFound),
//				Case($(instanceOf(CreditRecorded.class)), this::creditRecorded));
//
//	}
}
