package io.pismo.transaction_routine.core;

import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.core.converter.AccountRequestToAccountEntityConverter;
import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.service.impl.AccountFacadeImpl;
import io.pismo.transaction_routine.dataprovider.repository.AccountRepository;
import io.pismo.transaction_routine.mock.AccountFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit Test - Account Facade")
@ExtendWith(SpringExtension.class)
public class AccountFacadeTest {

    @SpyBean
    AccountFacadeImpl accountFacade;
    @MockBean
    AccountRepository accountRepository;
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

}