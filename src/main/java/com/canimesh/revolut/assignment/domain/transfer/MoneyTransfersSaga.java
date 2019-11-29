package com.canimesh.revolut.assignment.domain.transfer;

import java.util.UUID;

import com.canimesh.revolut.assignment.domain.account.event.AccountCredited;
import com.canimesh.revolut.assignment.domain.account.event.AccountDebitFailedDueToInsufficientFunds;
import com.canimesh.revolut.assignment.domain.account.event.AccountDebited;
import com.canimesh.revolut.assignment.eventbus.EventBus;

public class MoneyTransfersSaga {

    private final MoneyTransferRepository moneyTransferRepository;
    
    public MoneyTransfersSaga(EventBus eventBus,
                                  MoneyTransferRepository moneyTransferRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
        
        setUpSubscribers(eventBus);
    }

    private void setUpSubscribers(EventBus eventBus) {
		eventBus.subscribe(AccountDebited.class, (event) -> {this.recordDebit((AccountDebited)event);});
		eventBus.subscribe(AccountCredited.class, (event) -> {this.recordCredit((AccountCredited)event);});
		eventBus.subscribe(AccountDebitFailedDueToInsufficientFunds.class, (event) -> {this.recordFailedDebit((AccountDebitFailedDueToInsufficientFunds)event);});
		
	}

	public void recordDebit(AccountDebited accountDebited) {
        UUID transferId = accountDebited.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordDebit();
            moneyTransferRepository.save(transfer);
        });
    }

    public void recordCredit(AccountCredited accountCredited) {
    	UUID transferId = accountCredited.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordCredit();
            moneyTransferRepository.save(transfer);
        });
    }

    public void recordFailedDebit(AccountDebitFailedDueToInsufficientFunds debitFailed) {
    	UUID transferId = debitFailed.getTransferId();
        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            transfer.recordFailedDebit();
            moneyTransferRepository.save(transfer);
        });
    }


}
