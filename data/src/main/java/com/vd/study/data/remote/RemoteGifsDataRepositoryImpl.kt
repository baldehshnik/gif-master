package com.vd.study.data.remote

import androidx.paging.PagingData
import com.vd.study.core.dispatchers.IODispatcher
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.data.remote.entities.RemoteGifDataEntity
import com.vd.study.data.remote.sources.GifsApiDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteGifsDataRepositoryImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dataSource: GifsApiDataSource
) : RemoteGifsDataRepository {

    override suspend fun pagingReadGifs(): Flow<PagingData<RemoteGifDataEntity>> = withContext(ioDispatcher) {
        return@withContext dataSource.pagingReadGifs()
    }

    override suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<RemoteGifDataEntity>> = withContext(ioDispatcher) {
        return@withContext dataSource.pagingReadSearchGifs(query)
    }
}
