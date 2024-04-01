package com.vd.study.account.domain.repositories

import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.core.container.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun readAccount(email: String): Result<AccountEntity>

    suspend fun readLikedGifs(): Result<Flow<List<GifEntity>>>

    suspend fun readViewedGifs(): Result<Flow<List<GifEntity>>>

}
