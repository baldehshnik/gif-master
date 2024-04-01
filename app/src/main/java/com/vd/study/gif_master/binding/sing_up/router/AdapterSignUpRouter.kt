package com.vd.study.gif_master.binding.sing_up.router

import com.vd.study.gif_master.R
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import com.vd.study.sign_up.presentation.router.SignUpRouter
import javax.inject.Inject

class AdapterSignUpRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SignUpRouter {

    override fun navigateToMain() {
        globalNavComponentRouter.popToInclusive(R.id.signInFragment)
    }

    override fun popUpToSignIn() {
        globalNavComponentRouter.popBackStack()
    }

    override fun hideBottomBar() {
        globalNavComponentRouter.changeBottomAppBarVisibility(false)
    }
}
