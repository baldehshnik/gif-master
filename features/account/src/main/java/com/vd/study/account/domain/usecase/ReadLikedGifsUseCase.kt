package com.vd.study.account.domain.usecase

import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.domain.repositories.AccountRepository
import com.vd.study.core.IODispatcher
import com.vd.study.core.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadLikedGifsUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: AccountRepository
) {

    suspend operator fun invoke(accountId: Int): Result<Flow<List<GifEntity>>> =
        withContext(ioDispatcher) {
            return@withContext repository.readLikedGifs(accountId)
        }
}