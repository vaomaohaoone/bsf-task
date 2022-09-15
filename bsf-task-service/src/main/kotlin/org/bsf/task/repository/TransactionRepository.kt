package org.bsf.task.repository

import org.bsf.task.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, UUID> {
}