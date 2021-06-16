package io.pismo.transaction_routine.core.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_number", nullable = false)
    private Long documentNumber;

    @OneToMany(mappedBy = "accountEntity", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TransactionEntity> transactionEntity;

}
