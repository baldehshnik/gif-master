package com.vd.study.viewing.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.viewing.R
import com.vd.study.viewing.databinding.FragmentViewingBinding
import com.vd.study.viewing.domain.entities.GifAuthorEntity
import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.viewing.presentation.showToast
import com.vd.study.viewing.presentation.viewmodel.ViewingViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class ViewingFragment : Fragment(R.layout.fragment_viewing) {

    private val binding by viewBinding<FragmentViewingBinding>()

    private val viewModel by viewModels<ViewingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gif = getViewingGif()
        if (gif == null) {
            showToast(resources.getString(CoreResources.string.error))
            viewModel.popBackStack()
        } else {
            loadUI(gif)
            setListeners(gif)
        }
    }

    private fun setListeners(gif: GifEntity) = with(binding) {
        btnBack.setOnClickListener { viewModel.popBackStack() }
        btnShare.setOnClickListener { shareGif(gif) }
        btnLike.setOnClickListener { viewModel.updateGifByLike(gif) }
        btnSave.setOnClickListener { viewModel.updateGifBySave(gif) }
    }

    private fun shareGif(gif: GifEntity) {

    }

    private fun loadUI(gif: GifEntity) {
        loadAuthor(gif.author)

        Glide.with(requireContext())
            .load(gif.url)
            .into(binding.gif)
    }

    private fun getViewingGif() : GifEntity? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(INCOMING_GIF_KEY, GifEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(INCOMING_GIF_KEY)
        }
    }

    // add error placeholder
    private fun loadAuthor(author: GifAuthorEntity?) {
        if (author == null) {
            binding.textAuthorName.text = resources.getString(R.string.giphy)
            Glide.with(requireContext())
                .load(R.drawable.giphy)
                .into(binding.accountImage)
        } else {
            binding.textAuthorName.text = author.username
            Glide.with(requireContext())
                .load(author.avatarUrl)
                .into(binding.accountImage)
        }
    }

    companion object {
        const val INCOMING_GIF_KEY = "viewingGif"
    }
}












