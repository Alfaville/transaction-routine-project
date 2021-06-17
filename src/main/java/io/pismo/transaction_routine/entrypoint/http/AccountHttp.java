package io.pismo.transaction_routine.entrypoint.http;

import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.service.AccountFacade;
import io.pismo.transaction_routine.entrypoint.http.converter.AccountEntityToAccountResponseConverter;
import io.pismo.transaction_routine.entrypoint.http.openapi.AccountHttpOpenApi;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import io.pismo.transaction_routine.entrypoint.http.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/v1/accounts")
@RequiredArgsConstructor
public class AccountHttp implements AccountHttpOpenApi {

    private final AccountFacade accountFacade;
    private final AccountEntityToAccountResponseConverter toAccountResponse;

    @Override
    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest accountRequest) {
        final AccountEntity account = accountFacade.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(toAccountResponse.convert(account));
    }

    @Override
    @GetMapping(value = "/{accountId}")
    public ResponseEntity<AccountResponse> getById(@PathVariable("accountId") Long accountId) {
        var account = accountFacade.findAccount(accountId);
        if(account.isPresent()) {
            final AccountResponse response = toAccountResponse.convert(account.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}