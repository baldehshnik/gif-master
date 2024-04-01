package com.vd.study.account.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.vd.study.account.R
import com.vd.study.core.R as CoreResources
import com.vd.study.account.databinding.FragmentLikedGifsBinding
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.presentation.adapter.AccountGifsAdapter
import com.vd.study.account.presentation.adapter.OnLikedGifItemClickListener
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding

class LikedGifsFragment : Fragment(R.layout.fragment_liked_gifs), OnLikedGifItemClickListener {

    private val binding by viewBinding<FragmentLikedGifsBinding>()

    private var viewModel: AccountViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel == null) {
            // add empty widget
            requireContext().showToast(resources.getString(CoreResources.string.error))
        } else {
            startViewing(viewModel!!)
        }
    }

    override fun onClick(gif: GifEntity) {
        viewModel?.navigateToViewingFragment(gif)
    }

    private fun startViewing(viewModel: AccountViewModel) {
        viewModel.readLikedGifs()
        viewModel.likedGifsLiveData.observe(viewLifecycleOwner, ::handleLikedGifReading)
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
                val adapter = AccountGifsAdapter(data, this)
                binding.likedGifsList.adapter = adapter
                changeVisibility(false)
            }
        }
    }

    private fun changeVisibility(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.likedGifsList.isVisible = !isLoading
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
