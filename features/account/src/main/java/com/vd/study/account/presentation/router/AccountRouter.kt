package com.vd.study.account.presentation.router

import com.vd.study.account.domain.entities.GifEntity

interface AccountRouter {

    fun navigateToViewingFragment(gif: GifEntity)

    fun returnToSignInFragment()

    fun navigateToSettings()

}
