package com.vd.study.gif_master.binding.sign_in.router

import com.vd.study.gif_master.R
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.sign_in.presentation.router.SignInRouter
import javax.inject.Inject

class AdapterSignInRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SignInRouter {

    override fun navigateToRegistration() {
        globalNavComponentRouter.launch(R.id.signUpFragment)
    }

    override fun navigateToMain() {
        globalNavComponentRouter.popBackStack()
        globalNavComponentRouter.launch(R.id.homeFragment)
    }

    override fun hideBottomBar() {
        globalNavComponentRouter.changeBottomAppBarVisibility(false)
    }
}
