package com.vd.study.data.local.di

import com.vd.study.data.AccountsDataRepository
import com.vd.study.data.LocalGifsDataRepository
import com.vd.study.data.local.accounts.AccountsDataRepositoryImpl
import com.vd.study.data.local.gifs.LocalGifsDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface LocalRepositoriesModule {

    @Binds
    fun bindAccountsDataRepositoryImplToAccountsDataRepository(
        accountsDataRepositoryImpl: AccountsDataRepositoryImpl
    ): AccountsDataRepository

    @Binds
    fun bindLocalGifsDataRepositoryImplToLocalGifsDataRepository(
        localGifsDataRepositoryImpl: LocalGifsDataRepositoryImpl
    ): LocalGifsDataRepository
}