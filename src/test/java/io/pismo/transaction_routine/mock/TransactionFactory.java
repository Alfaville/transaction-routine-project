package io.pismo.transaction_routine.mock;

import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import io.pismo.transaction_routine.core.entity.OperationTypeEnum;
import io.pismo.transaction_routine.core.entity.TransactionEntity;
import io.pismo.transaction_routine.entrypoint.http.request.TransactionRequest;

import java.math.BigDecimal;

public enum TransactionFactory {
    INSTANCE;

    public TransactionEntity getTransactionSaque() {
        var transaction = new TransactionEntity();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("-50"));
        transaction.setAccountEntity(AccountFactory.INSTANCE.getAccountEntity());

        var operationType = new OperationTypeEntity();
        operationType.setId(3L);
        transaction.setOperationTypeEntity(operationType);
        return transaction;
    }

    public TransactionRequest getTransactionSaqueRequest() {
        var transaction = new TransactionRequest();
        transaction.setAmount(new BigDecimal("-50"));
        transaction.setOperationTypeId(OperationTypeEnum.SAQUE);
        return transaction;
    }

}
