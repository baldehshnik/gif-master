package com.vd.study.gif_master.binding.search.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.vd.study.data.RemoteGifsDataRepository
import com.vd.study.gif_master.binding.search.mappers.RemoteGifDataEntityToNetworkGifEntityMapper
import com.vd.study.search.domain.entities.NetworkGifEntity
import com.vd.study.search.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchAdapterRepository @Inject constructor(
    private val remoteRepository: RemoteGifsDataRepository,
    private val remoteGifDataEntityToNetworkGifEntityMapper: RemoteGifDataEntityToNetworkGifEntityMapper
) : SearchRepository {

    override suspend fun pagingReadSearchGifs(query: String): Flow<PagingData<NetworkGifEntity>> {
        return remoteRepository.pagingReadSearchGifs(query).map { pagingData ->
            pagingData.map(remoteGifDataEntityToNetworkGifEntityMapper::map)
        }
    }
}
