package io.pismo.transaction_routine.config.exception;

public class AccountCreditLimitExeception extends RuntimeException {

    public AccountCreditLimitExeception(String message) {
        super(message);
    }

}
