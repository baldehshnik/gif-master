package com.vd.study.gif_master.presentation.router

import android.util.Log
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vd.study.gif_master.R
import com.vd.study.gif_master.presentation.activity.ActivityRequired
import com.vd.study.gif_master.presentation.activity.MainActivity
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

    fun launch(direction: NavDirections) {
        getRootNavController().navigate(direction)
    }

    fun changeBottomAppBarVisibility(isVisible: Boolean) {
        (activity as MainActivity?)?.changeBottomBarVisibility(isVisible)
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