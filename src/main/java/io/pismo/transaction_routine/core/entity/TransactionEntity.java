package io.pismo.transaction_routine.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "TRANSACTION")
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "amount", nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @JoinColumn(name = "operation_type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OperationTypeEntity operationTypeEntity;

    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne
    private AccountEntity accountEntity;

}
