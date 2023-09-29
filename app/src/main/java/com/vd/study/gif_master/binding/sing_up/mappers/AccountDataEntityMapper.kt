package com.vd.study.gif_master.binding.sing_up.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.accounts.entities.AccountDataEntity
import com.vd.study.sign_up.domain.entities.AccountEntity

class AccountDataEntityMapper : Mapper<AccountEntity, AccountDataEntity> {

    override fun map(input: AccountEntity): AccountDataEntity {
        return AccountDataEntity(
            username = input.username,
            avatarUrl = input.avatarUrl,
            email = input.email,
            password = input.password
        )
    }
}