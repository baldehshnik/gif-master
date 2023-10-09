package com.vd.study.account.presentation.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vd.study.account.R
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var viewModelFactory: AccountViewModel.Factory

    private val viewModel: AccountViewModel by viewModels {
        AccountViewModel.provideFactory(viewModelFactory, "TODO")
    }

}