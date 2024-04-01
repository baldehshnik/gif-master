package com.vd.study.home.presentations.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.vd.study.core.global.APP_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.APP_THEME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.dialog.showNetworkWarningDialog
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.home.R
import com.vd.study.home.databinding.FragmentHomeBinding
import com.vd.study.home.domain.entities.FullGifEntity
import com.vd.study.home.presentations.adapter.GifsAdapter
import com.vd.study.home.presentations.adapter.OnGifItemClickListener
import com.vd.study.home.presentations.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnGifItemClickListener {

    private val binding by viewBinding<FragmentHomeBinding>()

    private val viewModel by viewModels<HomeViewModel>()

    private var gifsAdapter: GifsAdapter? = null

    private val connectivityManager = lazy {
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val networkStateCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val listItemsCount = gifsAdapter?.itemCount
            if (listItemsCount == null || listItemsCount < 1) {
                Handler(Looper.getMainLooper()).post {
                    setProgressVisibility(true)
                    viewModel.readGifs()
                }
            }
        }
    }

    private val listLoadStateListener = { loadState: CombinedLoadStates ->
        if (loadState.source.refresh is LoadState.NotLoading) {
            setProgressVisibility(false)
        } else if (loadState.source.refresh is LoadState.Error) {
            setProgressVisibility(false)
            requireContext().showNetworkWarningDialog(themeIdentifier)
        }
    }

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        viewModel.readGifsResult.observe(viewLifecycleOwner, ::handleReadingResult)
    }

    override fun onStart() {
        connectivityManager.value.registerDefaultNetworkCallback(networkStateCallback)
        super.onStart()
    }

    override fun onStop() {
        connectivityManager.value.unregisterNetworkCallback(networkStateCallback)
        super.onStop()
    }

    override fun onDestroyView() {
        gifsAdapter?.removeLoadStateListener(listLoadStateListener)
        gifsAdapter = null
        super.onDestroyView()
    }

    override fun onItemClick(gif: FullGifEntity) {
        viewModel.navigateToViewingFragment(gif)
    }

    private fun initUI() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        viewModel.showBottomBar()

        setProgressVisibility(true)

        gifsAdapter = GifsAdapter(this)
        binding.listGifs.adapter = gifsAdapter
        gifsAdapter?.addLoadStateListener(listLoadStateListener)

        binding.btnChangeTheme.setImageResource(
            if (themeIdentifier.isLightTheme) R.drawable.moon else R.drawable.round_wb_sunny
        )
        binding.btnChangeTheme.setOnClickListener { handleChangeAppThemeClick() }
    }

    private fun handleChangeAppThemeClick() {
        themeIdentifier.isLightTheme = !themeIdentifier.isLightTheme
        requireContext().getSharedPreferences(APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(APP_THEME, if (themeIdentifier.isLightTheme) 1 else 0)
            .apply()

        requireActivity().apply {
            setTheme(
                if (themeIdentifier.isLightTheme) CoreResources.style.Theme_Gifmaster_Core
                else CoreResources.style.Theme_Gifmaster_Core_Dark
            )
            recreate()
        }
    }

    private fun handleReadingResult(data: PagingData<FullGifEntity>) {
        lifecycleScope.launch {
            gifsAdapter?.submitData(data)
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }
}
