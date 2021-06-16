package io.pismo.transaction_routine.dataprovider.repository;

import io.pismo.transaction_routine.core.entity.TransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionEntity, Long> {
}