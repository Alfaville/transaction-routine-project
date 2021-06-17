package io.pismo.transaction_routine.entrypoint.http.converter;

import io.pismo.transaction_routine.core.entity.TransactionEntity;
import io.pismo.transaction_routine.entrypoint.http.response.AccountResponse;
import io.pismo.transaction_routine.entrypoint.http.response.TransactionResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class TransactionEntityToTransactionResponseConverter implements Converter<TransactionEntity, TransactionResponse> {

    @Override
    public TransactionResponse convert(TransactionEntity source) {
        return new TransactionResponse(source.getId());
    }

}