package io.pismo.transaction_routine.entrypoint.http.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountRequest {

    @NotBlank(message = "Document number is required")
    private String documentNumber;

}
