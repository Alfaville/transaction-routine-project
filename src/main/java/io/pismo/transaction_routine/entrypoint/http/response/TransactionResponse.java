package io.pismo.transaction_routine.entrypoint.http.response;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
public class TransactionResponse {
    Long transactionId;
}
