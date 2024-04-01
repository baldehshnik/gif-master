package com.vd.study.gif_master.binding.sing_up.repositories

import com.vd.study.core.container.Result
import com.vd.study.data.AccountsDataRepository
import com.vd.study.gif_master.binding.sing_up.mappers.AccountDataEntityMapper
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.domain.repositories.RegistrationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationAdapterRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val accountDataEntityMapper: AccountDataEntityMapper
) : RegistrationRepository {

    override suspend fun registerAccount(account: AccountEntity): Result<Boolean> {
        return accountsDataRepository.saveNewAccount(accountDataEntityMapper.map(account))
    }
}
