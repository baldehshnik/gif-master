package com.vd.study.settings.presentation.fragment

import androidx.fragment.app.Fragment
import com.vd.study.core.viewbinding.viewBinding
import com.vd.study.settings.R
import com.vd.study.settings.databinding.FragmentSettingsBinding
import com.vd.study.settings.presentation.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var factory: SettingsViewModel.Factory

    private val binding: FragmentSettingsBinding by viewBinding()

}