package com.vd.study.account.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentAccountBinding
import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.domain.entities.GifEntity
import com.vd.study.account.presentation.adapter.LikedGifsPagerAdapter
import com.vd.study.account.presentation.adapter.OnLikedGifItemClickListener
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.container.Result
import com.vd.study.core.global.ACCOUNT_EMAIL_FIELD_NAME
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    @Inject
    lateinit var viewModelFactory: AccountViewModel.Factory

    private var email: String? = null

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private val _viewModel: Lazy<AccountViewModel> by lazy {
        requireNotNull(email) { "Email must be initialized before accessing ViewModel" }
        viewModels {
            AccountViewModel.provideFactory(viewModelFactory, email!!)
        }
    }
    private val viewModel: AccountViewModel get() = _viewModel.value

    private val binding by viewBinding<FragmentAccountBinding>()

    private val onPagerStateListenerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            loadListsInfoLine(position == 0)
        }
    }

    private val onGifItemClickListener = object : OnLikedGifItemClickListener {
        override fun onClick(gif: GifEntity) {
            // open more detail fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEmail()
        loadUI()
        loadListeners()
        loadListsInfoLine(true)

        val adapter = LikedGifsPagerAdapter(this, viewModel)
        binding.viewPager.adapter = adapter

        viewModel.accountLiveValue.observe(viewLifecycleOwner, ::handleAccountReading)
    }

    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(onPagerStateListenerCallback)
        super.onDestroyView()
    }

    private fun loadListsInfoLine(isDefault: Boolean) {
        if (isDefault) {
            binding.line1.setBackgroundColor(Color.BLACK)
            binding.line2.setBackgroundColor(Color.GRAY)
        } else {
            binding.line1.setBackgroundColor(Color.GRAY)
            binding.line2.setBackgroundColor(Color.BLACK)
        }
    }

    private fun loadListeners() {
        binding.btnEdit.setOnClickListener {
            // open settings fragment
        }
        binding.btnLiked.setOnClickListener {
            loadListsInfoLine(false)
            binding.viewPager.setCurrentItem(1, true)
        }
        binding.btnViewed.setOnClickListener {
            loadListsInfoLine(true)
            binding.viewPager.setCurrentItem(0, true)
        }
        binding.viewPager.registerOnPageChangeCallback(onPagerStateListenerCallback)
    }

    private fun loadUI() {
        if (themeIdentifier.isLightTheme) {
            binding.screen.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.gray_background)
            )
            binding.btnEdit.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), CoreResources.color.black
            )
            binding.btnEdit.setTextColor(Color.WHITE)
        } else {
            binding.btnEdit.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), CoreResources.color.purple_200
            )
            binding.btnEdit.setTextColor(Color.BLACK)
        }
    }

    private fun getEmail() {
        val sharedPreferences = requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        email = sharedPreferences.getString(ACCOUNT_EMAIL_FIELD_NAME, "")
    }

    private fun handleAccountReading(result: Result<AccountEntity>) {
        when (result) {
            Result.Progress -> {
                changeScreenVisibility(true)
                changeLikedGifsReadingVisibility(true)
            }

            is Result.Error -> {
                changeScreenVisibility(false)
                changeLikedGifsReadingVisibility(false)
                requireContext().showToast(resources.getString(CoreResources.string.error))
            }

            is Result.Correct -> {
                val account = result.getOrNull()!!
                loadAccountUI(account)

                changeScreenVisibility(false)
                changeLikedGifsReadingVisibility(false)
                viewModel.readLikedGifs()
            }
        }
    }

    private fun loadAccountUI(account: AccountEntity) {
        binding.textAccountName.text = account.username
        binding.emailInfo.text = resources.getString(R.string.email_info, email)

        setDate(account.date)
        Glide.with(requireContext())
            .load(account.avatarUrl)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .into(binding.imageAccount)

        Log.i("MYTAG", account.avatarUrl)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate(date: Date) {
        val format = SimpleDateFormat("hh:mm dd.MM.yyyy")
        val viewingDate = format.format(date)
        binding.creationDateInfo.text = resources.getString(R.string.date_info, viewingDate)
    }

    private fun changeScreenVisibility(isProgress: Boolean) {
        binding.screenProgress.isVisible = isProgress
        binding.screen.isVisible = !isProgress
    }

    private fun changeLikedGifsReadingVisibility(isProgress: Boolean) {
        binding.viewPager.isVisible = !isProgress
        binding.listProgress.isVisible = isProgress
    }
}
