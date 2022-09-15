package org.bsf.task.entity

import java.math.BigInteger
import java.util.*
import java.util.Collections.emptyList
import javax.persistence.*

@Entity
@Table(schema = "bsf", name = "account")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    @Column(name = "name")
    var accountName: String? = null,
    @Column(name = "balance_sum")
    var balanceSum: BigInteger = BigInteger.ZERO,
    @Column(name = "user_id")
    var userId: String? = null,
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,
    @OneToMany(mappedBy = "receiver")
    var receiverHistory: List<TransactionEntity> = mutableListOf(),
    @OneToMany(mappedBy = "sender")
    var senderHistory: List<TransactionEntity> = mutableListOf()
)
