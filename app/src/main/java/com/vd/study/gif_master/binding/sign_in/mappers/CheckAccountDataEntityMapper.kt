package com.vd.study.gif_master.binding.sign_in.mappers

import com.vd.study.core.mapper.Mapper
import com.vd.study.data.local.accounts.entities.CheckAccountDataEntity
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import javax.inject.Inject

class CheckAccountDataEntityMapper @Inject constructor() :
    Mapper<CheckAccountEntity, CheckAccountDataEntity> {

    override fun map(input: CheckAccountEntity): CheckAccountDataEntity {
        return CheckAccountDataEntity(input.email, input.password)
    }
}
