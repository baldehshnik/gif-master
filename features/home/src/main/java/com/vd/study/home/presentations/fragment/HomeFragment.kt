package com.vd.study.home.presentations.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.home.R
import com.vd.study.home.databinding.FragmentHomeBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.presentations.adapter.OnGifItemClickListener
import com.vd.study.home.presentations.adapter.TestAdapter
import com.vd.study.home.presentations.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnGifItemClickListener {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private var testAdapter: TestAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        }

        initUI()
        viewModel.readGifsResult.observe(viewLifecycleOwner, ::handleReadingResult)
        viewModel.updateGifResult.observe(viewLifecycleOwner, ::handleUpdateResult)
    }

    override fun onDestroyView() {
        testAdapter = null
        super.onDestroyView()
    }

    override fun onLikeClick(gif: FullGifEntity) {
        viewModel.updateGif(gif.copy(isLiked = !gif.isLiked))
    }

    override fun onSaveClick(gif: FullGifEntity) {
        viewModel.updateGif(gif.copy(isSaved = !gif.isSaved))
    }

    override fun onShareClick(gif: FullGifEntity) {

    }

    override fun onItemClick(gif: FullGifEntity) {
        val view = layoutInflater.inflate(R.layout.gif_dialog, null)
        val gifImage = view.findViewById<ShapeableImageView>(R.id.gif)
        val accountImage = view.findViewById<ShapeableImageView>(R.id.imageAccount)

        Glide.with(requireContext())
            .load(gif.author?.avatarUrl)
            .into(accountImage)

        Glide.with(requireContext())
            .asGif()
            .load(gif.url)
            .into(gifImage)

        AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
            .show()
    }

    private fun initUI() {
        setProgressVisibility(true)
        testAdapter = TestAdapter(this)
        binding.listGifs.adapter = testAdapter
    }

    private fun handleUpdateResult(updatedGif: FullGifEntity?) {
        if (updatedGif == null) {
            requireContext().showToast(resources.getString(R.string.update_error))
        } else {

        }
    }

    private fun handleReadingResult(data: PagingData<FullGifEntity>) {
        lifecycleScope.launch {
            setProgressVisibility(false)
            testAdapter?.submitData(data)
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }
}