package io.pismo.transaction_routine.entrypoint.http.openapi;

import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import io.pismo.transaction_routine.entrypoint.http.request.TransactionRequest;
import io.pismo.transaction_routine.entrypoint.http.response.AccountResponse;
import io.pismo.transaction_routine.entrypoint.http.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = AccountHttpOpenApi.TAG, description = "Transactional accounts API")
public interface AccountHttpOpenApi {

    String TAG = "Account";

    @Operation(summary = "Create account", tags = {TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccountResponse.class)),
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = io.pismo.transaction_routine.entrypoint.http.response.ApiResponse.class)),
            }),
    })
    ResponseEntity<AccountResponse> create(@RequestBody AccountRequest accountRequest);

    @Operation(summary = "Find account by ID", tags = {TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AccountResponse.class)),
            }),
            @ApiResponse(responseCode = "204", description = "No Content", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }),
    })
    ResponseEntity<AccountResponse> getById(@Parameter(required = true, description = "account id") Long accountId);

    @Operation(summary = "Create transaction for an account", tags = {TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransactionResponse.class)),
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            }),
    })
    ResponseEntity<TransactionResponse> createTransaction(
            @PathVariable("accountId") Long accountId,
            @RequestBody TransactionRequest transactionRequest
    );
}