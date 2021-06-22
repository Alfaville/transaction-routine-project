package io.pismo.transaction_routine.dataprovider.repository;

import io.pismo.transaction_routine.config.exception.AccountCreditLimitExeception;
import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import io.pismo.transaction_routine.core.entity.TransactionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Integrated test Database - H2 Mode Postgresql")
public class DatabaseIntegrationTest {

    @Autowired
    private transient AccountRepository accountRepository;
    @Autowired
    private transient TransactionRepository transactionRepository;
    @Autowired
    private transient OperationTypeRepository operationTypeRepository;

    @Test
    @Order(1)
    void persistAccountWithSuccess() {
        AccountEntity account = new AccountEntity();
        account.setDocumentNumber("12345678900");

        final AccountEntity newAccount = accountRepository.save(account);

        assertNotNull(newAccount);
    }

    @Test
    @Order(2)
    void persistAccountWithoutDocumentNumberAndReturnError() {
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () ->
                accountRepository.save(new AccountEntity())
        );
        assertNotNull(exception.getMessage());
    }

    @Test
    @Order(3)
    void findAccountByIdAndReturnSuccess() {
        final Optional<AccountEntity> accountOp = accountRepository.findById(1L);

        assertNotNull(accountOp.get());
    }

    @Test
    @Order(4)
    void persistTransactionWithSuccess() {
        final Long compraAVista = 1L;
        final Optional<OperationTypeEntity> operationTypeOp = operationTypeRepository.findById(compraAVista);
        final Optional<AccountEntity> accountOp = accountRepository.findById(1L);

        final TransactionEntity transaction = transactionRepository.save(
                TransactionEntity.builder()
                        .accountEntity(accountOp.get())
                        .amount(new BigDecimal(-23.5))
                        .operationTypeEntity(operationTypeOp.get())
                        .build()
        );

        assertNotNull(transaction);
    }

    @Test
    @Order(5)
    void persistTransactionWithoutOperationTypeAndReturnError() {
        final Long compraAVista = 1L;
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            final Optional<OperationTypeEntity> operationTypeOp = operationTypeRepository.findById(compraAVista);
            transactionRepository.save(
                    TransactionEntity.builder()
                            .accountEntity(null)
                            .amount(new BigDecimal("63.5"))
                            .operationTypeEntity(operationTypeOp.get())
                            .build()
            );
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    @Order(6)
    void tryPersistAccountWithSameDocumentNumberAndReturnError() {
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            var account = new AccountEntity();
            account.setDocumentNumber("12345678900");
            accountRepository.save(account);
        });
        assertNotNull(exception.getMessage());
    }

    @Test
    @Order(7)
    void tryToPersistAccountWithAvailableCreditLimitPositive() {
        AccountEntity account = new AccountEntity();
        account.setDocumentNumber("123456789099");
        account.setAvailableCreditLimit(new BigDecimal("50.89"));

        final AccountEntity newAccount = accountRepository.save(account);

        assertNotNull(newAccount);
    }

    @Test
    @Order(8)
    void tryToPersistAccountWithAvailableCreditLimitNegative() {
        AccountCreditLimitExeception exception = assertThrows(AccountCreditLimitExeception.class, () -> {
            var account = new AccountEntity();
            account.setDocumentNumber("123456789087");
            account.setAvailableCreditLimit(new BigDecimal("-50"));
            accountRepository.save(account);
        });
        assertNotNull(exception.getMessage());
    }
}