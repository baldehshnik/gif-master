package com.vd.study.account.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vd.study.account.presentation.fragment.LikedGifsFragment
import com.vd.study.account.presentation.fragment.ViewedGifsFragment
import com.vd.study.account.presentation.viewmodel.AccountViewModel

class AccountGifsPagerAdapter(
    fragment: Fragment,
    private val viewModel: AccountViewModel
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ViewedGifsFragment.getInstance(viewModel)
        } else {
            LikedGifsFragment.getInstance(viewModel)
        }
    }
}
