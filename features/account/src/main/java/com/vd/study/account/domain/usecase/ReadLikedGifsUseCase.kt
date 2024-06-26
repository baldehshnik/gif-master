package com.vd.study.account.domain.usecase

import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.domain.repositories.AccountRepository
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.core.container.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadLikedGifsUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val repository: AccountRepository
) {

    suspend operator fun invoke(): Result<Flow<List<GifEntity>>> =
        withContext(ioDispatcher) {
            return@withContext repository.readLikedGifs()
        }
}
