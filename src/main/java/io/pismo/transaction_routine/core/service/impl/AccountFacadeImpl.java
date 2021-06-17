package io.pismo.transaction_routine.core.service.impl;

import io.pismo.transaction_routine.config.exception.EntityAlreadyExistExeception;
import io.pismo.transaction_routine.core.converter.AccountRequestToAccountEntityConverter;
import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.core.service.AccountFacade;
import io.pismo.transaction_routine.dataprovider.repository.AccountRepository;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final AccountRepository accountRepository;
    private final AccountRequestToAccountEntityConverter accountRequestToAccountEntityConverter;

    @Override
    public AccountEntity createAccount(AccountRequest accountRequest) {
        var account = accountRepository.findByDocumentNumber(accountRequest.getDocumentNumber());
        if(account.isEmpty()) {
            var newAccount = accountRequestToAccountEntityConverter.convert(accountRequest);
            return accountRepository.save(newAccount);
        }
        throw new EntityAlreadyExistExeception(String.format("Account already exists. ID: %d", account.get().getId()));
    }

    @Override
    public Optional<AccountEntity> findAccount(Long id) {
        return accountRepository.findById(id);
    }

}