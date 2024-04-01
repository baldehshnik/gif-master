package com.vd.study.gif_master.binding.account.mappers

import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.core.mapper.Mapper
import javax.inject.Inject

class AccountEntityMapper @Inject constructor() : Mapper<AccountDataEntity, AccountEntity> {

    override fun map(input: AccountDataEntity): AccountEntity {
        return AccountEntity(
            input.id, input.username, input.avatarUrl, input.date
        )
    }
}
