package com.vd.study.settings.domain.repositories

import com.vd.study.core.container.Result
import com.vd.study.settings.domain.entities.AccountEntity

interface SettingsRepository {

    suspend fun updateAccount(account: AccountEntity): Result<Boolean>

    suspend fun removeAccount(): Result<Boolean>

    suspend fun readAccount(): Result<AccountEntity>

    suspend fun readLikedGifsCount(): Result<Int>

    suspend fun readSavedGifsCount(): Result<Int>

}