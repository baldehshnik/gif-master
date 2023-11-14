package com.vd.study.gif_master.presentation.router

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.vd.study.gif_master.R
import com.vd.study.gif_master.presentation.activity.ActivityRequired
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalNavComponentRouter @Inject constructor() : ActivityRequired {

    private var activity: FragmentActivity? = null

    override fun onCreated(activity: FragmentActivity) {
        this.activity = activity
    }

    override fun onDestroyed() {
        activity = null
    }

    fun popBackStack() {
        getRootNavController().popBackStack()
    }

    fun launch(@IdRes destinationId: Int) {
        getRootNavController().navigate(destinationId)
    }

    fun popToInclusive(@IdRes destinationId: Int) {
        getRootNavController().popBackStack(destinationId, true)
    }

    private fun getRootNavController(): NavController {
        val fragmentManager = requireActivity().supportFragmentManager
        val navHost = fragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private fun requireActivity(): FragmentActivity = activity!!
}