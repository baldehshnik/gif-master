package com.vd.study.gif_master.binding.sign_in.di

import com.vd.study.gif_master.binding.sign_in.repositories.SignInAdapterRepository
import com.vd.study.sign_in.domain.repositories.SignInRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface SignInAdapterRepositoryModule {

    @Binds
    fun bindSignInAdapterRepositoryToSignInRepository(
        singInAdapterRepository: SignInAdapterRepository
    ): SignInRepository
}