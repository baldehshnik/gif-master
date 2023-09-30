package com.vd.study.gif_master.binding.sign_in.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.sign_in.domain.entities.AccountEntity

class AccountEntityMapper : Mapper<AccountDataEntity, AccountEntity> {

    override fun map(input: AccountDataEntity): AccountEntity {
        return AccountEntity(
            input.id,
            input.username,
            input.avatarUrl,
            input.email,
            input.password
        )
    }
}