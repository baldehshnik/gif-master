package com.vd.study.gif_master.binding.account.repositories

import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.domain.repositories.AccountRepository
import com.vd.study.core.container.Result
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.data.AccountsDataRepository
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.local.gifs.entities.LocalGifDataEntity
import com.vd.study.gif_master.binding.account.mappers.AccountEntityMapper
import com.vd.study.gif_master.binding.account.mappers.GifEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountAdapterRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val gifsDataRepository: LocalGifsDataRepository,
    private val accountEntityMapper: AccountEntityMapper,
    private val gifEntityMapper: GifEntityMapper,
    private val accountIdentifier: AccountIdentifier
) : AccountRepository {

    override suspend fun readAccount(email: String): Result<AccountEntity> {
        return accountsDataRepository.readAccount(email).suspendMap(accountEntityMapper::map)
    }

    override suspend fun readLikedGifs(): Result<Flow<List<GifEntity>>> {
        return gifsDataRepository.readLikedGifs(accountIdentifier.accountIdentifier)
            .suspendMap(::flowMapping)
    }

    override suspend fun readViewedGifs(): Result<Flow<List<GifEntity>>> {
        return gifsDataRepository.readViewedGifs(accountIdentifier.accountIdentifier)
            .suspendMap(::flowMapping)
    }

    private fun flowMapping(flow: Flow<List<LocalGifDataEntity>>): Flow<List<GifEntity>> {
        return flow.map { items ->
            items.map(gifEntityMapper::map)
        }
    }
}
