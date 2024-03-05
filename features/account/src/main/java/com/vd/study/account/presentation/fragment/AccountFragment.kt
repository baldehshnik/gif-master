package com.vd.study.account.presentation.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentAccountBinding
import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.presentation.adapter.LikedGifsPagerAdapter
import com.vd.study.account.presentation.adapter.OnLikedGifItemClickListener
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// test
@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var viewModelFactory: AccountViewModel.Factory

    private val viewModel: AccountViewModel by viewModels {
        AccountViewModel.provideFactory(viewModelFactory, "baldehshnik@gmail.com")
    }

    private val binding by viewBinding<FragmentAccountBinding>()

    private val onGifItemClickListener = object : OnLikedGifItemClickListener {
        override fun onClick(gif: GifEntity) {
            // open more detail fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEdit.setOnClickListener {
            // open settings fragment
        }

        val adapter = LikedGifsPagerAdapter(this, viewModel)
        binding.viewPager.adapter = adapter

        viewModel.readLikedGifsCount()
        viewModel.likedGifsCountLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.getOrNull().toString(), Toast.LENGTH_SHORT).show()
        }


        viewModel.accountLiveValue.observe(viewLifecycleOwner, ::handleAccountReading)
    }

    private fun handleAccountReading(result: Result<AccountEntity>) {
        when (result) {
            Result.Progress -> {
                changeScreenVisibility(true)
            }

            is Result.Error -> {
                // add
                changeScreenVisibility(false)

                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                // fix
                changeLikedGifsReadingVisibility(false)
            }

            is Result.Correct -> {
                val account = result.getOrNull()!! // fix
//                Toast.makeText(requireContext(), account.username, Toast.LENGTH_SHORT).show()
                binding.textAccountName.text = account.username

                changeScreenVisibility(false)
                viewModel.readLikedGifs()
            }
        }
    }

    private fun changeScreenVisibility(isProgress: Boolean) {
        binding.screenProgress.isVisible = isProgress
//        binding.screen.isVisible = !isProgress
    }

    private fun changeLikedGifsReadingVisibility(isProgress: Boolean) {
        binding.viewPager.isVisible = !isProgress
        binding.listProgress.isVisible = isProgress
    }
}