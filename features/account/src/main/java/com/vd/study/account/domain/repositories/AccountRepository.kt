package com.vd.study.account.domain.repositories

import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.core.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun readAccount(email: String): Result<AccountEntity>

    suspend fun readLikedGifs(accountId: Int): Result<Flow<List<GifEntity>>>

    suspend fun readLikedGifsCount(accountId: Int): Result<Int>

}