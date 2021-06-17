package io.pismo.transaction_routine.core;

import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationTypeEntityTest {

    @Test
    @DisplayName("Operation type PAYMENT with negative value amount with return positive value")
    void operation_type_payment_with_negative_value_amount_with_return_positive_value() {
        var PAGAMENTO = 4L;
        var operationType = new OperationTypeEntity();
        operationType.setId(PAGAMENTO);

        var amountValueReturned = operationType.checkValueType(new BigDecimal(-50));
        var amountValueExpected = new BigDecimal(50);

        assertEquals(amountValueExpected, amountValueReturned);
    }

    @Test
    @DisplayName("Operation type PAYMENT with positive value amount with return positive value")
    void operation_type_payment_with_positive_value_amount_with_return_positive_value() {
        var PAGAMENTO = 4L;
        var operationType = new OperationTypeEntity();
        operationType.setId(PAGAMENTO);

        var amountValueReturned = operationType.checkValueType(new BigDecimal(50));
        var amountValueExpected = new BigDecimal(50);

        assertEquals(amountValueExpected, amountValueReturned);
    }

    @Test
    @DisplayName("Operation type COMPRA_A_VISTA with negative value amount with return negative value")
    void operation_type_compra_a_vista_with_negative_value_amount_with_return_negative_value() {
        var COMPRA_A_VISTA = 1L;
        var operationType = new OperationTypeEntity();
        operationType.setId(COMPRA_A_VISTA);

        var amountValueReturned = operationType.checkValueType(new BigDecimal(-50));
        var amountValueExpected = new BigDecimal(-50);

        assertEquals(amountValueExpected, amountValueReturned);
    }

    @Test
    @DisplayName("Operation type SAQUE with positive value amount with return negative value")
    void operation_type_saque_with_positive_value_amount_with_return_negative_value() {
        var SAQUE = 3L;
        var operationType = new OperationTypeEntity();
        operationType.setId(SAQUE);

        var amountValueReturned = operationType.checkValueType(new BigDecimal(50));
        var amountValueExpected = new BigDecimal(-50);

        assertEquals(amountValueExpected, amountValueReturned);
    }

}
