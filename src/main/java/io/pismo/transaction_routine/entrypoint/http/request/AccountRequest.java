package io.pismo.transaction_routine.entrypoint.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountRequest {

    @JsonProperty("document_number")
    @NotBlank(message = "Document number is required")
    private String documentNumber;
}