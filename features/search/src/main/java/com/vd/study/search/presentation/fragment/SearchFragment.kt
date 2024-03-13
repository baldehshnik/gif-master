package com.vd.study.search.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
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
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.search.R
import com.vd.study.search.databinding.FragmentSearchBinding
import com.vd.study.search.presentation.adapter.SearchGifAdapter
import com.vd.study.search.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding<FragmentSearchBinding>()

    private val viewModel by viewModels<SearchViewModel>()

    private var adapter: SearchGifAdapter? = null

    private val isKeyboardVisible: Boolean
        get() = WindowInsetsCompat
            .toWindowInsetsCompat(binding.root.rootWindowInsets)
            .isVisible(WindowInsetsCompat.Type.ime())

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnBackPressListener()
        setEditTextListener()
        setListAdapter()

        viewModel.gifsReadingResult.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                launch { adapter?.submitData(it) }

                delay(500L)
                changeProgressVisibility(false)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEditTextListener() {
        binding.btnSearch.setOnClickListener {
            handleEditTextValue()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleEditTextValue()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setListAdapter() {
        adapter = SearchGifAdapter()
        binding.listGifs.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
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
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun changeProgressVisibility(isVisible: Boolean) {
        binding.progress.isVisible = isVisible
        binding.listGifs.isVisible = !isVisible
    }

    private fun setOnBackPressListener() {
        binding.btnBack.setOnClickListener {
            viewModel.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!isKeyboardVisible) {
                viewModel.popBackStack()
            }
        }
    }
}
