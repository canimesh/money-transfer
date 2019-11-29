package com.canimesh.revolut.assignment.domain.account;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.account.command.CreditAccount;
import com.canimesh.revolut.assignment.domain.account.command.DebitAccount;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransfer;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferRepository;
import com.canimesh.revolut.assignment.domain.transfer.command.RecordNotFoundAccount;
import com.canimesh.revolut.assignment.domain.transfer.event.DebitRecorded;
import com.canimesh.revolut.assignment.domain.transfer.event.MoneyTransferCreated;
import com.canimesh.revolut.assignment.eventbus.EventBus;

public class AccountsSaga {

    private AccountRepository accountRepository;
    private MoneyTransferRepository moneyTransferRepository;

    public AccountsSaga(EventBus eventBus,
                            AccountRepository accountRepository,
                            MoneyTransferRepository moneyTransferRepository) {
        
        this.accountRepository = accountRepository;
        this.moneyTransferRepository = moneyTransferRepository;
        setUpSubscribers(eventBus);
    }

    private void setUpSubscribers(EventBus eventBus) {
    	eventBus.subscribe(DebitRecorded.class, (event) -> this.credit((DebitRecorded)(event)));
    	eventBus.subscribe(MoneyTransferCreated.class, (event) -> this.debit((MoneyTransferCreated)(event)));
	}

    public void credit(DebitRecorded debitRecorded) {
        UUID toAccountId = debitRecorded.getDetails().getToAccountId();
        Optional<Account> accountOptional = accountRepository.findById(toAccountId);
        accountOptional.ifPresent(account -> creditAccount(debitRecorded, account));
    }

    public void debit(MoneyTransferCreated transferCreated) {
        UUID transferId = transferCreated.getAggregateId();
        UUID fromAccountId = transferCreated.getDetails().getFromAccountId();
        UUID toAccountId = transferCreated.getDetails().getToAccountId();

        moneyTransferRepository.findById(transferId).ifPresent(transfer -> {
            Optional<Account> fromAccountOptional = accountRepository.findById(fromAccountId);
            if (!fromAccountOptional.isPresent()) {
                recordNotFoundAccount(fromAccountId, transfer);
            }else {
            	Optional<Account> toAccountOptional = accountRepository.findById(toAccountId);
            	if (!toAccountOptional.isPresent()) {
                    recordNotFoundAccount(toAccountId, transfer);
                }else {
                	Account toDebit = fromAccountOptional.get();
                    debitAccount(transferCreated, toDebit);
                }
            	
            }
                moneyTransferRepository.save(transfer);
        });
    }

    private void recordNotFoundAccount(UUID accountId, MoneyTransfer transfer) {
        transfer.recordNotFoundAccount(
            new RecordNotFoundAccount(transfer.getId(), accountId));
    }

    private void creditAccount(DebitRecorded debitRecorded, Account account) {
        account.credit(
            new CreditAccount(
                debitRecorded.getDetails().getMoney(),
                debitRecorded.getAggregateId()));
        accountRepository.save(account);
    }

    private void debitAccount(MoneyTransferCreated transferCreated, Account toDebit) {
        toDebit.debit(
            new DebitAccount(
                transferCreated.getDetails().getMoney(),
                transferCreated.getAggregateId()));
        accountRepository.save(toDebit);
    }

}
