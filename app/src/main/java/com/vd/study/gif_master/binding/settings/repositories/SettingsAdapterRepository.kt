package com.vd.study.gif_master.binding.settings.repositories

import com.vd.study.core.container.Result
import com.vd.study.data.AccountsDataRepository
import com.vd.study.gif_master.binding.settings.mappers.AccountDataEntityMapper
import com.vd.study.gif_master.binding.settings.mappers.AccountEntityMapper
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.repositories.SettingsRepository
import javax.inject.Inject

class SettingsAdapterRepository @Inject constructor(
    private val repository: AccountsDataRepository,
    private val accountEntityMapper: AccountEntityMapper,
    private val accountDataEntityMapper: AccountDataEntityMapper
) : SettingsRepository {

    override suspend fun updateAccount(account: AccountEntity): Result<Boolean> {
        return repository.updateAccount(accountDataEntityMapper.map(account))
    }

    override suspend fun removeAccount(account: AccountEntity): Result<Boolean> {
        return repository.removeAccount(accountDataEntityMapper.map(account))
    }

    override suspend fun readAccount(email: String): Result<AccountEntity> {
        return repository.readAccount(email).suspendMap(accountEntityMapper::map)
    }
}
