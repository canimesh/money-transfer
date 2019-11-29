package com.canimesh.revolut.assignment.domain.transfer;

import java.util.Optional;
import java.util.UUID;

import com.canimesh.revolut.assignment.domain.transfer.command.CreateMoneyTransfer;

public interface MoneyTransferRepository {

    MoneyTransfer save(MoneyTransfer transfer);

    Optional<MoneyTransfer> findById(UUID id);

    MoneyTransfer create(CreateMoneyTransfer createMoneyTransfer);

}
