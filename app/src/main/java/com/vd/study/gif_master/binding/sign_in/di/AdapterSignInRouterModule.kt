package com.vd.study.gif_master.binding.sign_in.di

import com.vd.study.gif_master.binding.sign_in.router.AdapterSignInRouter
import com.vd.study.sign_in.presentation.router.SignInRouter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)

interface AdapterSignInRouterModule {

    @Binds
    fun bindAdapterSignInRouterToSignInRouter(adapterSignInRouter: AdapterSignInRouter): SignInRouter
}
