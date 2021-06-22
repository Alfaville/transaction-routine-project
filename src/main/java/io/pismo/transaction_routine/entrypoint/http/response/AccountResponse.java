package io.pismo.transaction_routine.entrypoint.http.response;

import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Getter
public class AccountResponse {

    String accountId;
    String documentNumber;
    BigDecimal limitCredit;

}
