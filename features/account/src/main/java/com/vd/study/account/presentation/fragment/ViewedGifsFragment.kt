package com.vd.study.account.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentViewedGifsBinding
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.presentation.viewbinding.viewBinding

class ViewedGifsFragment : Fragment(R.layout.fragment_viewed_gifs) {

    private val binding by viewBinding<FragmentViewedGifsBinding>()

    private var viewModel: AccountViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {

        @JvmStatic
        fun getInstance(accountViewModel: AccountViewModel): ViewedGifsFragment {
            return ViewedGifsFragment().also {
                it.viewModel = accountViewModel
            }
        }
    }

}