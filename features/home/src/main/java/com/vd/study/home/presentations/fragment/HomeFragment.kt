package com.vd.study.home.presentations.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.home.R
import com.vd.study.home.databinding.FragmentHomeBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.presentations.adapter.GifAdapter
import com.vd.study.home.presentations.adapter.OnGifItemClickListener
import com.vd.study.home.presentations.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnGifItemClickListener {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private var adapter: GifAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.readGifsResult.observe(viewLifecycleOwner, ::handleReadingResult)
    }

    override fun onDestroyView() {
        adapter = null
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

    private fun initUI() {
        setProgressVisibility(true)

        adapter = GifAdapter(this)
        binding.listGifs.adapter = adapter
    }

    private fun handleReadingResult(data: PagingData<FullGifEntity>) {
        lifecycleScope.launch {
            setProgressVisibility(false)
            adapter?.submitData(data)
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }
}