package org.bsf.task.mapper

import org.bsf.task.dto.AccountCreateDto
import org.bsf.task.dto.AccountDto
import org.bsf.task.entity.AccountEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AccountMapper {

    @Mappings(
        Mapping(source = "initialSum", target = "balanceSum")
    )
    fun mapAccountCreateDtoToEntity(accountCreateDto: AccountCreateDto): AccountEntity

    fun mapEntityToDto(entity: AccountEntity): AccountDto
}