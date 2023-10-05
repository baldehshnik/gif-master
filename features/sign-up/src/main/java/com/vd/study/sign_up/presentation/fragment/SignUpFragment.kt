package com.vd.study.sign_up.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vd.study.core.viewbinding.viewBinding
import com.vd.study.sign_up.R
import com.vd.study.sign_up.databinding.FragmentSignUpBinding
import com.vd.study.sign_up.presentation.viewmodel.SignUpViewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()

    private val viewBinding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}