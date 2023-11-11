package com.vd.study.gif_master.presentation.activity

import androidx.fragment.app.FragmentActivity

interface ActivityRequired {

    fun onCreated(activity: FragmentActivity)

    fun onDestroyed()

}