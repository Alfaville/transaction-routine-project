package io.pismo.transaction_routine.entrypoint.http.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class TransactionRequest {
    @NotNull
    private Long operationTypeId;
    @NotNull
    private BigDecimal amount;
}