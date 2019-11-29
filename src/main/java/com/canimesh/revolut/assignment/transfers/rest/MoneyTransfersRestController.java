package com.canimesh.revolut.assignment.transfers.rest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.canimesh.revolut.assignment.domain.transfer.MoneyTransfer;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferService;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;
import com.canimesh.revolut.assignment.domain.transfer.vo.StateChange;
import com.canimesh.revolut.assignment.exception.ApplicationRuntimeException;
import com.canimesh.revolut.assignment.transfers.rest.dto.StatusSnapshot;
import com.canimesh.revolut.assignment.transfers.rest.dto.TransferDetailsResponse;
import com.canimesh.revolut.assignment.transfers.rest.dto.TransferRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoneyTransfersRestController {

	private final MoneyTransferService moneyTransferService;

	public String transfer(TransferRequest transferRequest) {
		if(Objects.isNull(transferRequest) || transferRequest.getAmount() <= 0 || StringUtils.isBlank(transferRequest.getFromAccount()) || StringUtils.isBlank(transferRequest.getToAccount())) {
        	throw new ApplicationRuntimeException(400, "Invalid request");
        }
		MoneyTransfer transferMoney = moneyTransferService.transferMoney(UUID.fromString(transferRequest.getFromAccount()), UUID.fromString(transferRequest.getToAccount()), new Money(transferRequest.getAmount()));
		return transferMoney.getId().toString();
	}

	public TransferDetailsResponse get(String transferId) {
		Optional<MoneyTransfer> optionalMoneyTransfer = moneyTransferService.get(UUID.fromString(transferId));
		return convert(optionalMoneyTransfer.orElseThrow(() -> {return new ApplicationRuntimeException(404, "Transfer id not found");}));
	}

	private TransferDetailsResponse convert(MoneyTransfer moneyTransfer) {
		TransferDetailsResponse transferDetailsResponse = new TransferDetailsResponse();
		transferDetailsResponse.setFromAccount(moneyTransfer.getDetails().getFromAccountId().toString());
		transferDetailsResponse.setToAccount(moneyTransfer.getDetails().getToAccountId().toString());
		transferDetailsResponse.setAmount(moneyTransfer.getDetails().getMoney().getAmount());
		transferDetailsResponse.setId(moneyTransfer.getId().toString());
		transferDetailsResponse.setStatusChanges(convert(moneyTransfer.getStateChanges()));
		return transferDetailsResponse;
	}

	private List<StatusSnapshot> convert(List<StateChange> stateChanges) {
		return stateChanges.stream().map(s -> {return new StatusSnapshot(s.getInstant().toString(), s.getState().toString());}).collect(Collectors.toList());
	}


}
