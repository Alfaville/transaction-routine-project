package io.pismo.transaction_routine.core.service;

import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.entity.TransactionEntity;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import io.pismo.transaction_routine.entrypoint.http.request.TransactionRequest;

import java.util.Optional;

public interface AccountFacade {
    AccountEntity createAccount(AccountRequest accountRequest);
    Optional<AccountEntity> findAccount(Long id);
    TransactionEntity createTransactionForAccount(Long accountId, TransactionRequest transactionRequest);
}
