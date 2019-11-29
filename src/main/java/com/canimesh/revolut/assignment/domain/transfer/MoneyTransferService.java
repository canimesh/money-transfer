package com.canimesh.revolut.assignment.domain.transfer;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.command.CreateMoneyTransfer;
import com.canimesh.revolut.assignment.domain.transfer.vo.Money;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;


    public MoneyTransfer transferMoney(UUID fromAccountId, UUID toAccountId,
                                       Money amount) {
        return moneyTransferRepository.create(
            new CreateMoneyTransfer(TransferDetails.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .money(amount)
                .build()));
    }


	public Optional<MoneyTransfer> get(UUID transferId) {
		return moneyTransferRepository.findById(transferId);
		
	}

}
