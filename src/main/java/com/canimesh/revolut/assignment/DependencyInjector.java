package com.canimesh.revolut.assignment;

import com.canimesh.revolut.assignment.account.rest.AccountsRestController;
import com.canimesh.revolut.assignment.account.rest.AccountsRestHandler;
import com.canimesh.revolut.assignment.bound.context.AccountsContext;
import com.canimesh.revolut.assignment.domain.account.AccountRepository;
import com.canimesh.revolut.assignment.domain.account.AccountService;
import com.canimesh.revolut.assignment.domain.account.AccountsSaga;
import com.canimesh.revolut.assignment.domain.account.repo.AccountDataStore;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferRepository;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransferService;
import com.canimesh.revolut.assignment.domain.transfer.MoneyTransfersSaga;
import com.canimesh.revolut.assignment.eventbus.DefaultEventBus;
import com.canimesh.revolut.assignment.eventbus.EventBus;
import com.canimesh.revolut.assignment.eventstore.EventSourcesAccountRepository;
import com.canimesh.revolut.assignment.eventstore.EventSourcesMoneyTransferRepository;
import com.canimesh.revolut.assignment.eventstore.EventStore;
import com.canimesh.revolut.assignment.eventstore.EventStreamsStore;
import com.canimesh.revolut.assignment.eventstore.InMemoryEventStore;
import com.canimesh.revolut.assignment.eventstore.TransactionManager;
import com.canimesh.revolut.assignment.read.accounts.AccountsInfoRetriever;
import com.canimesh.revolut.assignment.read.accounts.AccountsUpdateEventListener;
import com.canimesh.revolut.assignment.read.accounts.InMemoryAccountsInfo;
import com.canimesh.revolut.assignment.transfers.rest.MoneyTransfersRestController;
import com.canimesh.revolut.assignment.transfers.rest.TransfersRestHandler;

import lombok.Getter;

@Getter
public class DependencyInjector {

	private AccountsRestHandler accountsRestHandler;

	private TransfersRestHandler transfersRestHandler;

	private final static DependencyInjector INSTANCE = new DependencyInjector();

	static DependencyInjector instance() {
		return INSTANCE;
	}

	private DependencyInjector() {
		setUp();
	}

	private void setUp() {
		EventStreamsStore eventStreamsStore = new EventStreamsStore();
		EventBus eventBus = new DefaultEventBus();
		TransactionManager txManager = new TransactionManager();
		EventStore eventStore = new InMemoryEventStore(eventStreamsStore, eventBus, txManager);
		AccountRepository accountRepository = new EventSourcesAccountRepository(eventStore);
		AccountService accountService = new AccountService(accountRepository);
		AccountsContext accounts = new AccountsContext(accountService);
		AccountDataStore accountDataStore = new AccountDataStore();
		AccountsInfoRetriever accountsInfo = new InMemoryAccountsInfo(accountDataStore);
		AccountsRestController accountsRestController = new AccountsRestController(accountsInfo, accounts);
		AccountsUpdateEventListener accountsUpdateEventListener = new AccountsUpdateEventListener(eventBus,
				accountDataStore);
		MoneyTransferRepository moneyTransferRepository = new EventSourcesMoneyTransferRepository(eventStore);
		AccountsSaga accountsSaga = new AccountsSaga(eventBus, accountRepository, moneyTransferRepository);
		MoneyTransfersSaga moneyTransfersSaga = new MoneyTransfersSaga(eventBus, moneyTransferRepository);
		this.accountsRestHandler = new AccountsRestHandler(accountsRestController);

		MoneyTransferService moneyTransferService = new MoneyTransferService(moneyTransferRepository);
		MoneyTransfersRestController transfersRestController = new MoneyTransfersRestController(moneyTransferService);
		this.transfersRestHandler = new TransfersRestHandler(transfersRestController);

	}
	

}
