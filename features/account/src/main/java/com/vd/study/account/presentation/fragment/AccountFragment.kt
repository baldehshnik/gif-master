package com.vd.study.account.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.vd.study.account.R
import com.vd.study.account.databinding.FragmentAccountBinding
import com.vd.study.account.domain.entities.AccountEntity
import com.vd.study.account.presentation.adapter.AccountGifsPagerAdapter
import com.vd.study.account.presentation.viewmodel.AccountViewModel
import com.vd.study.core.container.Result
import com.vd.study.core.global.ACCOUNT_EMAIL_FIELD_NAME
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.global.setDefaultLoginValues
import com.vd.study.core.presentation.resources.getColorStateListValue
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

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private var email: String? = null

    private val _viewModel: Lazy<AccountViewModel> by lazy {
        requireNotNull(email) { resources.getString(R.string.email_must_be_initialized) }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!getEmail()) return
        loadUI()
        loadListeners()

        viewModel.accountLiveValue.observe(viewLifecycleOwner, ::handleAccountReading)
    }

    override fun onStart() {
        binding.viewPager.registerOnPageChangeCallback(onPagerStateListenerCallback)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.viewPager.setCurrentItem(0, true)
    }

    override fun onStop() {
        binding.viewPager.unregisterOnPageChangeCallback(onPagerStateListenerCallback)
        super.onStop()
    }

    private fun loadListsInfoLine(isDefault: Boolean) {
        val activeColor = if (themeIdentifier.isLightTheme) Color.BLACK else Color.GRAY
        val inactiveColor = if (themeIdentifier.isLightTheme) Color.GRAY else Color.BLACK

        if (isDefault) {
            binding.line1.setBackgroundColor(activeColor)
            binding.line2.setBackgroundColor(inactiveColor)
        } else {
            binding.line1.setBackgroundColor(inactiveColor)
            binding.line2.setBackgroundColor(activeColor)
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
    }

    private fun loadUI() {
        if (themeIdentifier.isLightTheme) setLightThemeElements() else setDarkThemeElements()
        val adapter = AccountGifsPagerAdapter(this, viewModel)
        binding.viewPager.adapter = adapter
    }

    private fun setDarkThemeElements() = with(binding) {
        btnEdit.backgroundTintList = requireContext().getColorStateListValue(CoreResources.color.purple_200)
        btnEdit.setTextColor(Color.BLACK)
    }

    private fun setLightThemeElements() = with(binding) {
        screen.setBackgroundColor(requireContext().getColor(R.color.gray_background))
        btnEdit.backgroundTintList = requireContext().getColorStateListValue(CoreResources.color.black)
        btnEdit.setTextColor(Color.WHITE)
    }

    private fun getEmail(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        email = sharedPreferences.getString(ACCOUNT_EMAIL_FIELD_NAME, "")

        if (email == null || email?.isBlank() == true) {
            sharedPreferences.setDefaultLoginValues()
            viewModel.returnToSignInFragment()
            return false
        }

        return true
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

    private fun loadAccountUI(account: AccountEntity) = with(binding) {
        textAccountName.text = account.username
        emailInfo.text = resources.getString(R.string.email_info, email)

        setDate(account.date)
        Glide.with(requireContext())
            .load(account.avatarUrl)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .centerCrop()
            .into(imageAccount)
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
