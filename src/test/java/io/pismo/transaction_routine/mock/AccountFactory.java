package io.pismo.transaction_routine.mock;

import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;

import java.math.BigDecimal;

public enum AccountFactory {
    INSTANCE;

    public AccountEntity getAccountEntity() {
        var account = new AccountEntity();
        account.setId(1L);
        account.setDocumentNumber("12345678900");
        account.setAvailableCreditLimit(new BigDecimal("50"));
        return account;
    }

    public AccountRequest getAccountRequest() {
        var account = new AccountRequest();
        account.setDocumentNumber("12345678900");
        return account;
    }
}
