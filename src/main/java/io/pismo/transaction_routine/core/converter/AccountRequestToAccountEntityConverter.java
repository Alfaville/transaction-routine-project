package io.pismo.transaction_routine.core.converter;

import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.entrypoint.http.request.AccountRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class AccountRequestToAccountEntityConverter implements Converter<AccountRequest, AccountEntity> {

    @Override
    public AccountEntity convert(AccountRequest source) {
        final AccountEntity account = new AccountEntity();
        account.setDocumentNumber(source.getDocumentNumber());
        return account;
    }

}