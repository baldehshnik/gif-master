package com.vd.study.gif_master.binding.settings.repositories

import com.vd.study.core.container.Result
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.data.AccountsDataRepository
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.gif_master.binding.settings.mappers.AccountDataEntityMapper
import com.vd.study.gif_master.binding.settings.mappers.AccountEntityMapper
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.domain.repositories.SettingsRepository
import javax.inject.Inject

class SettingsAdapterRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val gifsDataRepository: LocalGifsDataRepository,
    private val accountEntityMapper: AccountEntityMapper,
    private val accountDataEntityMapper: AccountDataEntityMapper,
    private val accountIdentifier: AccountIdentifier
) : SettingsRepository {

    override suspend fun readLikedGifsCount(): Result<Int> {
        return gifsDataRepository.readLikedGifsCount(accountIdentifier.accountIdentifier)
    }

    override suspend fun readSavedGifsCount(): Result<Int> {
        return gifsDataRepository.readSavedGifsCount(accountIdentifier.accountIdentifier)
    }

    override suspend fun updateAccount(account: AccountEntity): Result<Boolean> {
        return accountsDataRepository.updateAccount(accountDataEntityMapper.map(account))
    }

    override suspend fun removeAccount(): Result<Boolean> {
        return accountsDataRepository.removeAccount(accountIdentifier.accountIdentifier)
    }

    override suspend fun readAccount(): Result<AccountEntity> {
        return accountsDataRepository.readAccountById(accountIdentifier.accountIdentifier).suspendMap(accountEntityMapper::map)
    }
}
