package com.vd.study.search.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.vd.study.core.dispatchers.Dispatchers
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.resources.getColorStateListValue
import com.vd.study.core.presentation.resources.getColorValue
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.search.R
import com.vd.study.search.databinding.FragmentSearchBinding
import com.vd.study.search.domain.entities.GifEntity
import com.vd.study.search.presentation.adapter.OnGifItemClickListener
import com.vd.study.search.presentation.adapter.SearchGifAdapter
import com.vd.study.search.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), OnGifItemClickListener {

    private val binding by viewBinding<FragmentSearchBinding>()

    private val viewModel by viewModels<SearchViewModel>()

    private var adapter: SearchGifAdapter? = null

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    @Inject
    lateinit var dispatchers: Dispatchers
    private val isKeyboardVisible: Boolean
        get() = WindowInsetsCompat
            .toWindowInsetsCompat(binding.root.rootWindowInsets)
            .isVisible(WindowInsetsCompat.Type.ime())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
        viewModel.gifsReadingResult.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch(dispatchers.mainDispatcher) {
                adapter?.submitData(it)
            }
        }
    }

    override fun onClick(gif: GifEntity) {
        viewModel.navigateToViewingFragment(gif)
    }

    private fun setUI() = with(binding.etSearchLayout) {
        if (themeIdentifier.isLightTheme) setLightThemeElements() else setDarkThemeElements()

        setListAdapter()
        setEditTextListener()
        setOnBackPressListener()
    }

    private fun setDarkThemeElements() = with(binding) {
        btnSearch.setColorFilter(Color.WHITE)
        btnBack.setColorFilter(Color.WHITE)

        etSearchLayout.defaultHintTextColor = requireContext().getColorStateListValue(CoreResources.color.white)
        etSearchLayout.hintTextColor = requireContext().getColorStateListValue(CoreResources.color.second_background)
        etSearchLayout.boxStrokeColor = requireContext().getColorValue(CoreResources.color.second_background)
        etSearchLayout.boxStrokeWidth = 0
    }

    private fun setLightThemeElements() = with(binding) {
        btnSearch.setColorFilter(Color.BLACK)
        btnBack.setColorFilter(Color.BLACK)

        val blackColorStateList = requireContext().getColorStateListValue(CoreResources.color.black)
        etSearchLayout.defaultHintTextColor = blackColorStateList
        etSearchLayout.hintTextColor = blackColorStateList
        etSearchLayout.boxStrokeColor = requireContext().getColorValue(CoreResources.color.black)
        etSearchLayout.boxStrokeWidth = 1
    }

    private fun setEditTextListener() = with(binding) {
        btnSearch.setOnClickListener { handleEditTextValue() }
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleEditTextValue()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setListAdapter() {
        adapter = SearchGifAdapter(this)
        binding.listGifs.adapter = adapter

        adapter?.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                changeProgressVisibility(false)
            } else if (loadState.source.refresh is LoadState.Error) {
                changeProgressVisibility(false)
                requireContext().showToast(resources.getString(CoreResources.string.error))
            }
        }
    }

    private fun handleEditTextValue() {
        val value = binding.etSearch.text
        if (value.isNullOrBlank()) {
            requireContext().showToast(resources.getString(R.string.field_must_not_be_empty))
            binding.etSearch.requestFocus()
        } else {
            changeProgressVisibility(true)
            viewModel.readSearchGifs(value.toString())
            hideKeyboard()
            binding.etSearch.clearFocus()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun changeProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }

    private fun setOnBackPressListener() {
        binding.btnBack.setOnClickListener { viewModel.popBackStack() }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!isKeyboardVisible) viewModel.popBackStack()
        }
    }
}
