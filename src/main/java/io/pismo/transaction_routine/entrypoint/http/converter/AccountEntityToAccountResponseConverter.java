package io.pismo.transaction_routine.entrypoint.http.converter;

import io.pismo.transaction_routine.core.entity.AccountEntity;
import io.pismo.transaction_routine.entrypoint.http.response.AccountResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class AccountEntityToAccountResponseConverter implements Converter<AccountEntity, AccountResponse> {

    @Override
    public AccountResponse convert(AccountEntity source) {
        return new AccountResponse(source.getId().toString(), source.getDocumentNumber());
    }

}