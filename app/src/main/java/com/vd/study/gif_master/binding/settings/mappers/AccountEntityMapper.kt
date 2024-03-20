package com.vd.study.gif_master.binding.settings.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.settings.domain.entities.AccountEntity
import javax.inject.Inject

class AccountEntityMapper @Inject constructor() : Mapper<AccountDataEntity, AccountEntity> {
    override fun map(input: AccountDataEntity): AccountEntity {
        return AccountEntity(
            input.id,
            input.username,
            input.avatarUrl,
            input.email,
            input.password,
            input.date
        )
    }
}