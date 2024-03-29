package com.vd.study.viewing.presentation.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.viewing.R
import com.vd.study.viewing.databinding.FragmentViewingBinding
import com.vd.study.viewing.domain.entities.GifAuthorEntity
import com.vd.study.viewing.domain.entities.GifEntity
import com.vd.study.viewing.presentation.viewmodel.ViewingViewModel
import com.vd.study.viewing.presentation.worker.GifWorker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class ViewingFragment : Fragment(R.layout.fragment_viewing) {

    private var _gif: GifEntity? = null
    private val gif: GifEntity
        get() = checkNotNull(_gif) {
            resources.getString(R.string.viewing_gif_not_initialized)
        }

    private val binding by viewBinding<FragmentViewingBinding>()

    private val viewModel by viewModels<ViewingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _gif = getViewingGif()
        if (_gif == null) {
            requireContext().showToast(resources.getString(CoreResources.string.error))
            viewModel.popBackStack()
        } else {
            loadUI()
            setLikeColor()
            setListeners()
            saveViewingGif()
        }
    }

    override fun onDestroyView() {
        viewModel.changeBottomBarTheme(true)
        super.onDestroyView()
    }

    private fun saveViewingGif() {
        _gif = gif.copy(isViewed = true)
        val updateGifWorkRequest = OneTimeWorkRequestBuilder<GifWorker>()
            .setInputData(gif.createWorkData())
            .build()
        WorkManager.getInstance(requireContext()).enqueue(updateGifWorkRequest)
    }

    private fun setListeners() = with(binding) {
        btnBack.setOnClickListener { viewModel.popBackStack() }
        btnShare.setOnClickListener { shareGifFile() }
        btnLike.setOnClickListener { handleGifLike() }
        btnSave.setOnClickListener { handleGifSave() }
    }

    private fun setLikeColor() {
        if (gif.isLiked) {
            binding.btnLike.setImageResource(R.drawable.heart_red)
            binding.btnLike.setColorFilter(ContextCompat.getColor(requireContext(), R.color.like))
        } else {
            binding.btnLike.setImageResource(R.drawable.heart_white_border)
            binding.btnLike.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    CoreResources.color.white
                )
            )
        }
    }

    private fun handleGifLike() {
        _gif = gif.copy(isLiked = !gif.isLiked)
        requireContext().showToast(gif.isLiked.toString())
        setLikeColor()

        val updateGifWorkRequest = OneTimeWorkRequestBuilder<GifWorker>()
            .setInputData(gif.createWorkData())
            .build()
        WorkManager.getInstance(requireContext()).enqueue(updateGifWorkRequest)
    }

    private fun handleGifSave() {
        _gif = gif.copy(isSaved = !gif.isSaved)
        binding.btnSave.setImageResource(if (gif.isSaved) R.drawable.round_bookmark else R.drawable.round_bookmark_border)

        val updateGifWorkRequest = OneTimeWorkRequestBuilder<GifWorker>()
            .setInputData(gif.createWorkData())
            .build()
        WorkManager.getInstance(requireContext()).enqueue(updateGifWorkRequest)
    }

    private fun shareGifFile() {
        Glide.with(this)
            .asFile()
            .load(gif.url)
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    shareGifFile(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    requireContext().showToast(resources.getString(CoreResources.string.error))
                }
            })
    }

    private fun shareGifFile(gifFile: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            gifFile
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.view_gif_here, gif.url))
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                resources.getString(R.string.share_gif_image)
            )
        )
    }

    private fun loadUI() {
        viewModel.changeBottomBarTheme(false)
        loadAuthor(gif.author)

        Glide.with(requireContext())
            .load(CoreResources.drawable.giphy)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .centerCrop()
            .into(binding.imageGiphyProvider)

        Glide.with(requireContext())
            .load(gif.url)
            .error(CoreResources.drawable.image_error)
            .into(binding.gif)
    }

    private fun getViewingGif(): GifEntity? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(INCOMING_GIF_KEY, GifEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(INCOMING_GIF_KEY)
        }
    }

    private fun loadAuthor(author: GifAuthorEntity?) {
        if (author == null) {
            binding.textAuthorName.text = resources.getString(R.string.giphy)
            Glide.with(requireContext())
                .load(R.drawable.giphy)
                .centerCrop()
                .error(R.drawable.account_loading_error)
                .into(binding.accountImage)
        } else {
            binding.textAuthorName.text = author.username
            Glide.with(requireContext())
                .load(author.avatarUrl)
                .centerCrop()
                .placeholder(CoreResources.drawable.placeholder_gray_gradient)
                .error(R.drawable.account_loading_error)
                .into(binding.accountImage)
        }
    }

    companion object {
        const val INCOMING_GIF_KEY = "viewingGif"
    }
}
