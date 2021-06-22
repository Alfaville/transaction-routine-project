package io.pismo.transaction_routine.core.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum OperationTypeEnum {

    COMPRA_A_VISTA(1L, Type.DEBIT),
    COMPRA_PARCELADA(2L, Type.DEBIT),
    SAQUE(3L, Type.DEBIT),
    PAGAMENTO(4L, Type.CREDIT)
    ;

    public final Long identity;
    public final Type type;

    enum Type {
        CREDIT,
        DEBIT
    }

    public BigDecimal checkValueType(BigDecimal amount) {
        if(type.equals(Type.CREDIT)) {
            return amount.abs();
        } else {
            return amount.abs().negate();
        }
    }

}