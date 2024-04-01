package com.vd.study.gif_master.binding.account.di

import com.vd.study.account.domain.repositories.AccountRepository
import com.vd.study.gif_master.binding.account.repositories.AccountAdapterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AccountsAdapterRepositoryModule {

    @Binds
    fun bindAccountsAdapterRepositoryToAccountRepository(accountsAdapterRepository: AccountAdapterRepository): AccountRepository
}
