package io.pismo.transaction_routine.core;

import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.config.exception.EntityNotFoundExeception;
import io.pismo.transaction_routine.core.converter.AccountRequestToAccountEntityConverter;
import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import io.pismo.transaction_routine.core.entity.TransactionEntity;
import io.pismo.transaction_routine.core.service.impl.AccountFacadeImpl;
import io.pismo.transaction_routine.dataprovider.repository.AccountRepository;
import io.pismo.transaction_routine.dataprovider.repository.OperationTypeRepository;
import io.pismo.transaction_routine.dataprovider.repository.TransactionRepository;
import io.pismo.transaction_routine.mock.AccountFactory;
import io.pismo.transaction_routine.mock.TransactionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.IllegalFormatConversionException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit Test - Account Facade")
@ExtendWith(SpringExtension.class)
public class AccountFacadeTest {

    @SpyBean
    private transient AccountFacadeImpl accountFacade;
    @MockBean
    private transient AccountRepository accountRepository;
    @MockBean
    private transient OperationTypeRepository operationTypeRepository;
    @MockBean
    private transient TransactionRepository transactionRepository;
    @SpyBean
    AccountRequestToAccountEntityConverter accountRequestToAccountEntityConverter;

    @Test
    @DisplayName("Create valid account")
    void create_valid_account() {
        var account = AccountFactory.INSTANCE.getAccountEntity();

        //WHEN
        when(accountRepository.findByDocumentNumber(anyString()))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any(AccountEntity.class)))
                .thenReturn(account);

        var newAccount = accountFacade.createAccount(AccountFactory.INSTANCE.getAccountRequest());

        assertAll("Account created",
                () -> assertEquals("12345678900", newAccount.getDocumentNumber()),
                () -> assertEquals(1L, newAccount.getId())
        );

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
        verify(accountRepository, times(1)).findByDocumentNumber(anyString());
    }

    @Test
    @DisplayName("Try to create account and throw exception")
    void try_create_account_throw_exception() {
        var account = AccountFactory.INSTANCE.getAccountEntity();

        //WHEN
        when(accountRepository.findByDocumentNumber(anyString()))
                .thenReturn(Optional.of(account));

        EntityAlreadyExistExeception entityAlreadyExistExeception = assertThrows(
                EntityAlreadyExistExeception.class,
                () -> accountFacade.createAccount(AccountFactory.INSTANCE.getAccountRequest())
        );

        assertEquals("Account already exists. ID: 1", entityAlreadyExistExeception.getMessage());
        verify(accountRepository, times(1)).findByDocumentNumber(anyString());
    }

    @Test
    @DisplayName("Create valid transaction")
    void create_valid_transaction() {
        var accountId = 1L;
        var transaction = TransactionFactory.INSTANCE.getTransactionSaque();
        var account = AccountFactory.INSTANCE.getAccountEntity();
        var operationType = new OperationTypeEntity();
        operationType.setId(3L);

        //WHEN
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(anyLong()))
                .thenReturn(Optional.of(operationType));
        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(transaction);

        var newTransaction = accountFacade.createTransactionForAccount(accountId, TransactionFactory.INSTANCE.getTransactionSaqueRequest());

        assertAll("Transaction created",
                () -> assertEquals(1L, newTransaction.getId()),
                () -> assertEquals(new BigDecimal("-50"), newTransaction.getAmount()),
                () -> assertNotNull(newTransaction.getAccountEntity()),
                () -> assertNotNull(newTransaction.getOperationTypeEntity())
        );

        verify(accountRepository, times(1)).findById(anyLong());
        verify(operationTypeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Try create transaction with empty account throw exception")
    void try_create_transaction_with_empty_account_throw_exception() {
        var accountId = 1L;

        //WHEN
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        EntityNotFoundExeception entityNotFoundExeception = assertThrows(
                EntityNotFoundExeception.class,
                () -> accountFacade.createTransactionForAccount(accountId, TransactionFactory.INSTANCE.getTransactionSaqueRequest())
        );

        assertEquals("Account with ID 1 doesn't exists", entityNotFoundExeception.getMessage());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Try create transaction with empty operation type throw exception")
    void try_create_transaction_with_empty_operation_type__throw_exception() {
        var accountId = 1L;
        var account = AccountFactory.INSTANCE.getAccountEntity();

        //WHEN
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));
        when(operationTypeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        EntityNotFoundExeception entityNotFoundExeception = assertThrows(
                EntityNotFoundExeception.class,
                () -> accountFacade.createTransactionForAccount(accountId, TransactionFactory.INSTANCE.getTransactionSaqueRequest())
        );

        assertEquals("Operation type with ID 3 doesn't exists", entityNotFoundExeception.getMessage());
        verify(accountRepository, times(1)).findById(anyLong());
        verify(operationTypeRepository, times(1)).findById(anyLong());
    }
}