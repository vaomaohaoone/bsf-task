package org.bsf.task.service

import org.bsf.task.dto.AccountCreateDto
import org.bsf.task.dto.AccountDto
import org.bsf.task.dto.DeleteAccountDto
import org.bsf.task.dto.UpdateAccountNameDto
import org.bsf.task.entity.AccountEntity
import org.bsf.task.enums.TransactionType
import org.bsf.task.exception.BsfBusinessException
import org.bsf.task.exception.EntityDeletedException
import org.bsf.task.mapper.AccountMapper
import org.bsf.task.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val transactionService: TransactionService,
    private val accountMapper: AccountMapper
) {

    @Autowired
    lateinit var accountService: AccountService

    @Transactional
    fun createAccount(dto: AccountCreateDto): AccountDto {
        return accountMapper.mapAccountCreateDtoToEntity(dto).let { entity ->
            accountRepository.save(entity).let { account ->
                accountMapper.mapEntityToDto(account)
            }
        }
    }

    @Transactional
    fun updateAccountName(dto: UpdateAccountNameDto): AccountDto {
        val old = getAccountByIdOrThrow(dto.id)
        old.accountName = dto.accountName
        return accountRepository.save(old).let { account ->
            accountMapper.mapEntityToDto(account)
        }
    }

    fun getAccount(id: UUID): AccountDto =
        accountService.getAccountByIdOrThrow(id).let { account ->
            accountMapper.mapEntityToDto(account)
        }

    @Transactional(readOnly = true)
    fun getAccountByIdOrThrow(id: UUID): AccountEntity =
        accountRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Account with id=$id not found") }
            .apply {
                if (isDeleted) {
                    throw EntityDeletedException("Access to deleted account entity with id=$id")
                }
            }

    @Transactional
    fun deleteAccountById(dto: DeleteAccountDto) {
        val deleteCandidate = getAccountByIdOrThrow(dto.deleteAccountId)
        if (deleteCandidate.balanceSum != BigInteger.ZERO) {
            val targetCandidate = getAccount(dto.targetAccountId!!)
            if (deleteCandidate.userId == targetCandidate.userId) {
                transactionService.createTransaction(
                    dto.deleteAccountId,
                    dto.targetAccountId!!,
                    deleteCandidate.balanceSum,
                    TransactionType.WITHDRAW
                )
            } else
                throw BsfBusinessException("Incorrect user id on target account (should be equals)")
        }
        deleteCandidate.isDeleted = true
        accountRepository.save(deleteCandidate)
    }

    @Transactional(readOnly = true)
    fun listAccounts(): List<AccountDto> = accountRepository.findAll().asSequence()
        .filter { account -> !account.isDeleted }
        .map { account ->
            accountMapper.mapEntityToDto(account)
        }.toList()

}