package com.vd.study.home.presentations.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.home.R
import com.vd.study.home.databinding.FragmentHomeBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.presentations.adapter.OnGifItemClickListener
import com.vd.study.home.presentations.adapter.GifsAdapter
import com.vd.study.home.presentations.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnGifItemClickListener {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private var gifsAdapter: GifsAdapter? = null

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
        gifsAdapter = null
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
        viewModel.navigateToViewingFragment(gif)
    }

    private fun initUI() {
        viewModel.showBottomBar()
        setProgressVisibility(true)
        gifsAdapter = GifsAdapter(this)
        binding.listGifs.adapter = gifsAdapter
    }

    private fun handleUpdateResult(updatedGif: FullGifEntity?) {
        if (updatedGif == null) {
            requireContext().showToast(resources.getString(R.string.update_error))
        } else {

        }
    }

    private fun handleReadingResult(data: PagingData<FullGifEntity>) {
        lifecycleScope.launch {
            launch { gifsAdapter?.submitData(data) }

            delay(500L)
            setProgressVisibility(false)
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }
}