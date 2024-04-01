package com.vd.study.gif_master.binding.sing_up.di

import com.vd.study.gif_master.binding.sing_up.repositories.RegistrationAdapterRepository
import com.vd.study.sign_up.domain.repositories.RegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RegistrationAdapterRepositoryModule {

    @Binds
    fun bindRegistrationAdapterRepositoryToRegistrationRepository(
        registrationAdapterRepository: RegistrationAdapterRepository
    ): RegistrationRepository
}
