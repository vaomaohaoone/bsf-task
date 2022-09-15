package org.bsf.task.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.JPQLQuery
import org.bsf.task.dto.SearchCriteriaTransactionDto
import org.bsf.task.entity.QTransactionEntity
import org.bsf.task.entity.TransactionEntity
import org.bsf.task.exception.BsfBusinessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

@Repository
class TransactionPagingRepository : QuerydslRepositorySupport(TransactionEntity::class.java) {

    fun findBySearchCriteria(criteria: SearchCriteriaTransactionDto): Page<TransactionEntity> {
        val pageable = with(criteria.page) {
            PageRequest.of(number, size)
        }

        val preparedQuery = prepareQuerySafety()
        val predicates = buildConditional(criteria)
        val orderSpecifiers = getOrderSpecifier(criteria)

        val jpqlQuery = preparedQuery
            .from(QTransactionEntity.transactionEntity)
            .where(predicates)
            .orderBy(*orderSpecifiers.toTypedArray())

        val paginationQuery = querydsl!!.applyPagination(pageable, preparedQuery)
        return PageableExecutionUtils.getPage(paginationQuery.fetch(), pageable) { jpqlQuery.fetchCount() }
    }

    private fun buildConditional(criteria: SearchCriteriaTransactionDto): BooleanBuilder {
        return with(BooleanBuilder()) {
            and(applicationConditional(criteria))
        }
    }

    private fun applicationConditional(criteria: SearchCriteriaTransactionDto): BooleanBuilder {
        val qTransactionEntity = QTransactionEntity.transactionEntity
        return with(BooleanBuilder()) {
            and(criteria.receiverId?.let { qTransactionEntity.receiver.id.eq(it) })
            and(criteria.senderId?.let { qTransactionEntity.sender.id.eq(it) })
            and(criteria.transactionType?.let { qTransactionEntity.transactionType.eq(it) })
            and(criteria.sum?.let { it.notEmptyRange()?.let { qTransactionEntity.sum.between(it.from, it.to) } })
            and(criteria.date?.let { it.notEmptyRange()?.let { qTransactionEntity.createdOn.between(it.from, it.to) } })
        }
    }

    private fun getOrderSpecifier(criteria: SearchCriteriaTransactionDto): List<OrderSpecifier<*>> {
        val qTransactionEntity = QTransactionEntity.transactionEntity

        return if (criteria.sortBy == null) {
            listOf()
        } else {
            val sort = requireNotNull(criteria.sortBy)
            val sortOrder = Order.valueOf(sort.order)
            val orderSpecifier = when (sort.field) {
                "sum" -> OrderSpecifier(sortOrder, qTransactionEntity.sum)
                "date" -> OrderSpecifier(sortOrder, qTransactionEntity.createdOn)
                "type" -> OrderSpecifier(sortOrder, qTransactionEntity.transactionType)
                else -> throw BsfBusinessException("Unsupported sort field=${sort.field}")
            }
            listOf(orderSpecifier)
        }
    }

    private fun prepareQuerySafety(): JPQLQuery<TransactionEntity> =
        querydsl?.createQuery() ?: throw RuntimeException()


}