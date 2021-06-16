package io.pismo.transaction_routine.dataprovider.repository;

import io.pismo.transaction_routine.core.entity.OperationTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends CrudRepository<OperationTypeEntity, Long> {
}