package com.vd.study.account.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentLikedGifsBinding
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.presentation.viewbinding.viewBinding

class LikedGifsFragment : Fragment(R.layout.fragment_liked_gifs) {

    private val binding by viewBinding<FragmentLikedGifsBinding>()

    private var viewModel: AccountViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {

        @JvmStatic
        fun getInstance(accountViewModel: AccountViewModel): LikedGifsFragment {
            return LikedGifsFragment().also {
                it.viewModel = accountViewModel
            }
        }
    }

}