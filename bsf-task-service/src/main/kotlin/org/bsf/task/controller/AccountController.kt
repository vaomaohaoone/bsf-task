package org.bsf.task.controller

import org.bsf.task.dto.AccountCreateDto
import org.bsf.task.dto.AccountDto
import org.bsf.task.dto.DeleteAccountDto
import org.bsf.task.dto.UpdateAccountNameDto
import org.bsf.task.service.AccountService
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class AccountController(
    private val accountService: AccountService
) : AccountApi {

    override fun createAccount(dto: AccountCreateDto): AccountDto =
        accountService.createAccount(dto)

    override fun updateAccountName(dto: UpdateAccountNameDto): AccountDto =
        accountService.updateAccountName(dto)

    override fun getAccountById(accountId: UUID): AccountDto =
        accountService.getAccount(accountId)

    override fun deleteAccountById(dto: DeleteAccountDto) = accountService.deleteAccountById(dto)

    override fun listAccounts(): List<AccountDto> = accountService.listAccounts()

}