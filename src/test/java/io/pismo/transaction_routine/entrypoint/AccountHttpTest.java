package io.pismo.transaction_routine.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.core.service.AccountFacade;
import io.pismo.transaction_routine.entrypoint.http.AccountHttp;
import io.pismo.transaction_routine.entrypoint.http.converter.AccountEntityToAccountResponseConverter;
import io.pismo.transaction_routine.entrypoint.http.converter.TransactionEntityToTransactionResponseConverter;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import io.pismo.transaction_routine.entrypoint.http.request.TransactionRequest;
import io.pismo.transaction_routine.mock.AccountFactory;
import io.pismo.transaction_routine.mock.TransactionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AccountHttp.class)
@AutoConfigureMockMvc
@DisplayName("Account API Test")
public class AccountHttpTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private AccountFacade accountFacade;
    @SpyBean
    private AccountEntityToAccountResponseConverter toAccountResponse;
    @SpyBean
    private TransactionEntityToTransactionResponseConverter toTransactionResponse;

    static final String BASE_URL = "/v1/accounts";

    @Test
    @DisplayName("Try to create a non-existent account and return success")
    void try_create_a_non_existent_account_and_return_ok() throws Exception {
        //GIVEN
        var account = AccountFactory.INSTANCE.getAccountEntity();
        var accountRequest = AccountFactory.INSTANCE.getAccountRequest();

        //WHEN
        when(accountFacade.createAccount(any(AccountRequest.class)))
                .thenReturn(account);

        ResultActions resultActions = mockMvc.perform(
                post(new URI(BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(accountRequest))
        );

        var newAccount = accountFacade.createAccount(accountRequest);

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id").value(newAccount.getId()))
                .andExpect(jsonPath("$.document_number").value(newAccount.getDocumentNumber()))
        ;
    }

    @Test
    @DisplayName("Try to create a non-existent account without document number and return bad request")
    void try_create_a_non_existent_account_without_document_number_and_return_bad_request() throws Exception {
        //GIVEN
        var accountRequest = AccountFactory.INSTANCE.getAccountRequest();
        accountRequest.setDocumentNumber(null);

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                post(new URI(BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(accountRequest))
        );

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("Try to create a existent account and return error")
    void try_create_a_existent_account_and_return_error() throws Exception {
        //GIVEN
        var accountRequest = AccountFactory.INSTANCE.getAccountRequest();
        var exception = new EntityAlreadyExistExeception("Account already exists. ID: 1");

        //WHEN
        when(accountFacade.createAccount(any(AccountRequest.class)))
                .thenThrow(exception);

        ResultActions resultActions = mockMvc.perform(
                post(new URI(BASE_URL))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(accountRequest))
        );

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.message").isNotEmpty())
        ;
    }

    @Test
    @DisplayName("Get an account by ID and return OK")
    void get_account_by_id_and_return_ok() throws Exception {
        //GIVEN
        var accountId = 12345678900L;
        var account = AccountFactory.INSTANCE.getAccountEntity();
        when(accountFacade.findAccount(anyLong()))
                .thenReturn(Optional.of(account));

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                get(new URI(BASE_URL.concat("/12345678900")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        var foundAccount = accountFacade.findAccount(accountId);

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id").value(foundAccount.get().getId()))
                .andExpect(jsonPath("$.document_number").value(foundAccount.get().getDocumentNumber()))
        ;
    }

    @Test
    @DisplayName("Get an account by ID and return No Content")
    void get_account_by_id_and_return_no_content() throws Exception {
        //GIVEN
        var accountId = 99999999999L;
        when(accountFacade.findAccount(anyLong()))
                .thenReturn(Optional.empty());

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                get(new URI(BASE_URL.concat("/99999999999")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        var foundAccount = accountFacade.findAccount(accountId);

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @DisplayName("Try to create a non-existent transaction for an account and return success")
    void try_create_a_non_existent_transaction_for_account_and_return_ok() throws Exception {
        //GIVEN
        var transaction = TransactionFactory.INSTANCE.getTransactionSaque();
        var transactionRequest = TransactionFactory.INSTANCE.getTransactionSaqueRequest();

        //WHEN
        when(accountFacade.createTransactionForAccount(anyLong(), any(TransactionRequest.class)))
                .thenReturn(transaction);

        ResultActions resultActions = mockMvc.perform(
                post(new URI(BASE_URL) + "/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(transactionRequest))
        );

        var newTransaction = accountFacade.createTransactionForAccount(1L, transactionRequest);

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_id").value(newTransaction.getId()))
        ;
    }

    @Test
    @DisplayName("Try to create a non-existent transaction for an account without amount value and return bad request")
    void try_create_a_non_existent_transaction_for_account_without_amount_value_and_return_bad_request() throws Exception {
        //GIVEN
        var transaction = TransactionFactory.INSTANCE.getTransactionSaque();
        var transactionRequest = TransactionFactory.INSTANCE.getTransactionSaqueRequest();
        transactionRequest.setAmount(null);

        //WHEN
        ResultActions resultActions = mockMvc.perform(
                post(new URI(BASE_URL) + "/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(transactionRequest))
        );

        //THEN
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("amount must not be null"))
        ;
    }
}