package io.pismo.transaction_routine.core.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Table(name = "OPERATION_TYPE")
public class OperationTypeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    public BigDecimal checkValueType(BigDecimal amount) {
        boolean isPositiveAmount = (id.equals(TransactionEnum.PAGAMENTO.identity));
        if(isPositiveAmount) {
            return amount.abs();
        } else {
            return amount.abs().negate();
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum TransactionEnum {

        COMPRA_A_VISTA(1L),
        COMPRA_PARCELADA(2L),
        SAQUE(3L),
        PAGAMENTO(4L)
        ;

        public final Long identity;
    }
}