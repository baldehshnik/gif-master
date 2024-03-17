package com.vd.study.account.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentViewedGifsBinding
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.presentation.adapter.LikedGifsAdapter
import com.vd.study.account.presentation.adapter.OnLikedGifItemClickListener
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding

class ViewedGifsFragment : Fragment(R.layout.fragment_viewed_gifs), OnLikedGifItemClickListener {

    private val binding by viewBinding<FragmentViewedGifsBinding>()

    private var viewModel: AccountViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel == null) {
            requireContext().showToast("NULL")
        } else {
            startViewing(viewModel!!)
        }
    }

    override fun onClick(gif: GifEntity) {

    }

    private fun startViewing(viewModel: AccountViewModel) {
        viewModel.readViewedGifs()
        viewModel.viewedGifsLiveData.observe(viewLifecycleOwner, ::handleLikedGifReading)
    }

    private fun handleLikedGifReading(result: Result<List<GifEntity>>) {
        when (result) {
            Result.Progress -> {
                changeVisibility(true)
            }

            is Result.Error -> {
                // add handler
                changeVisibility(false)
            }

            is Result.Correct -> {
                // fix (show empty result)
                val data = result.getOrNull() ?: return
                val adapter = LikedGifsAdapter(data, this)
                binding.viewedGifsList.adapter = adapter
                changeVisibility(false)
            }
        }
    }

    private fun changeVisibility(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.viewedGifsList.isVisible = !isLoading
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