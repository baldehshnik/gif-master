package com.vd.study.search.domain.usecases

import androidx.paging.PagingData
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.search.domain.repositories.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PagingReadSearchGifsUseCase @Inject constructor(
    private val repository: SearchRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(query: String): Flow<PagingData<NetworkGifEntity>> = withContext(ioDispatcher) {
        return@withContext repository.pagingReadSearchGifs(query)
    }
}