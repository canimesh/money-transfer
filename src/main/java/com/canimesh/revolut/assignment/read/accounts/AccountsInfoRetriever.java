package com.canimesh.revolut.assignment.read.accounts;

import java.util.Optional;

public interface AccountsInfoRetriever {

    Optional<AccountInfo> get(String accountId);
}
