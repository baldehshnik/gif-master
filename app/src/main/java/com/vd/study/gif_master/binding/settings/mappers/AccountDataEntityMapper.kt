package com.vd.study.gif_master.binding.settings.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.settings.domain.entities.AccountEntity

class AccountDataEntityMapper : Mapper<AccountEntity, AccountDataEntity> {
    override fun map(input: AccountEntity): AccountDataEntity {
        return AccountDataEntity(
            input.id,
            input.username,
            input.avatarUrl,
            input.email,
            input.password
        )
    }
}