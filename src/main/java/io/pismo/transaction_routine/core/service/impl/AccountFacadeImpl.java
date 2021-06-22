package io.pismo.transaction_routine.core.service.impl;

import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.config.exception.EntityNotFoundExeception;
import io.pismo.transaction_routine.core.converter.AccountRequestToAccountEntityConverter;
import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import io.pismo.transaction_routine.core.entity.OperationTypeEnum;
import io.pismo.transaction_routine.core.entity.TransactionEntity;
import io.pismo.transaction_routine.core.service.AccountFacade;
import io.pismo.transaction_routine.dataprovider.repository.AccountRepository;
import io.pismo.transaction_routine.dataprovider.repository.OperationTypeRepository;
import io.pismo.transaction_routine.dataprovider.repository.TransactionRepository;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import io.pismo.transaction_routine.entrypoint.http.request.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final AccountRepository accountRepository;
    private final OperationTypeRepository operationTypeRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRequestToAccountEntityConverter accountRequestToAccountEntityConverter;

    @Override
    public AccountEntity createAccount(AccountRequest accountRequest) {
        var account = accountRepository.findByDocumentNumber(accountRequest.getDocumentNumber());
        if (account.isPresent()) {
            throw new EntityAlreadyExistExeception(String.format("Account already exists. ID: %d", account.get().getId()));
        } else {
            var newConverter = accountRequestToAccountEntityConverter.convert(accountRequest);
            var newAccount = accountRepository.save(newConverter);
            log.info(String.format("Account with ID %d created successfully!", newConverter.getId()));
            return newAccount;
        }
    }

    @Override
    public Optional<AccountEntity> findAccount(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional
    public TransactionEntity createTransactionForAccount(Long accountId, TransactionRequest transactionRequest) {
        var accountAsync = supplyAsync(() -> accountRepository.findById(accountId));
        var operationTypeAsync = supplyAsync(() -> operationTypeRepository.findById(transactionRequest.getOperationTypeId().getIdentity()));

        allOf(accountAsync, operationTypeAsync).join();

        var account = accountAsync.join();
        if (account.isEmpty()) {
            throw new EntityNotFoundExeception(String.format("Account with ID %d doesn't exists", accountId));
        }

        var operationType = operationTypeAsync.join();
        if (operationType.isEmpty()) {
            throw new EntityNotFoundExeception(String.format("Operation type with ID %d doesn't exists", transactionRequest.getOperationTypeId().getIdentity()));
        }
        final OperationTypeEntity operationTypeEntity = operationType.get();

        final BigDecimal amount = transactionRequest.getOperationTypeId().checkValueType(transactionRequest.getAmount());

        final AccountEntity updateAccount = account.get();
        updateAvailableLimitCredit(updateAccount, amount);

        var transaction = TransactionEntity.builder()
                .operationTypeEntity(operationType.get())
                .accountEntity(account.get())
                .amount(amount)
                .build();

        var newTransaction = transactionRepository.save(transaction);
        log.info(String.format("Transaction with ID %d created successfully!", newTransaction.getId()));
        return newTransaction;
    }

    private void updateAvailableLimitCredit(AccountEntity updateAccount, BigDecimal amount) {
        updateAccount.setAvailableCreditLimit(updateAccount.getAvailableCreditLimit().add(amount));
        accountRepository.save(updateAccount);
    }

}