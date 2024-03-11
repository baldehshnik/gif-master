package com.vd.study.gif_master.binding.account.di

import com.vd.study.account.presentation.router.AccountRouter
import com.vd.study.gif_master.binding.account.router.AdapterAccountRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface AdapterAccountRouterModule {

    @Binds
    fun bindAdapterAccountRouterToAccountRouter(
        adapterAccountRouter: AdapterAccountRouter
    ): AccountRouter
}