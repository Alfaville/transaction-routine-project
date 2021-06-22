package io.pismo.transaction_routine.entrypoint.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.pismo.transaction_routine.core.entity.OperationTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class TransactionRequest {
    @NotNull
    @JsonProperty("operation_type_id")
    private OperationTypeEnum operationTypeId;
    @NotNull
    private BigDecimal amount;
}