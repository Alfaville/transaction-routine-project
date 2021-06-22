package io.pismo.transaction_routine.core.entity;

import io.pismo.transaction_routine.config.exception.AccountCreditLimitExeception;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "available_credit_limit")
    private BigDecimal availableCreditLimit = BigDecimal.ZERO;

    @OneToMany(mappedBy = "accountEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TransactionEntity> transactionEntity;

    public void setAvailableCreditLimit(BigDecimal availableCreditLimit) {
        if(availableCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountCreditLimitExeception("The account value cannot be less than zero.");
        }
        this.availableCreditLimit = availableCreditLimit;
    }
}