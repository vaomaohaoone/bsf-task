package org.bsf.task.entity

import org.bsf.task.enums.TransactionType
import org.springframework.data.annotation.CreatedDate
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(schema = "bsf", name = "transaction")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "account_receiver_id", referencedColumnName = "id", updatable = false)
    var receiver: AccountEntity? = null,
    @ManyToOne
    @JoinColumn(name = "account_sender_id", referencedColumnName = "id", updatable = false)
    var sender: AccountEntity? = null,
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    var transactionType: TransactionType? = null,
    @Column(name = "sum", nullable = false, updatable = false)
    var sum: BigInteger,
    @CreatedDate
    @Column(name = "created_on", nullable = false, updatable = false)
    var createdOn: LocalDateTime? = LocalDateTime.now()
)
