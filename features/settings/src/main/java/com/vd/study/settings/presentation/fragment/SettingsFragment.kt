package com.vd.study.settings.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.vd.study.core.container.Result
import com.vd.study.core.global.APP_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.APP_THEME
import com.vd.study.core.global.SHOW_NETWORK_WARNING
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.global.setDefaultLoginValues
import com.vd.study.core.presentation.image.deleteOldImages
import com.vd.study.core.presentation.image.saveImageToInternalStorage
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.utils.ACCOUNT_IMAGE_FILE_NAME
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.settings.R
import com.vd.study.settings.databinding.FragmentSettingsBinding
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.presentation.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    private val viewModel: SettingsViewModel by viewModels()

    private var account: AccountEntity? = null

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private val pickAccountImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                val newImageName = "${account?.username}${System.currentTimeMillis()}$ACCOUNT_IMAGE_FILE_NAME"
                binding.accountImage.setImageURI(uri)
                requireContext().deleteOldImages(newImageName, account?.username.toString())
                requireContext().saveImageToInternalStorage(
                    uri, newImageName,
                    resultHandler = { imagePath ->
                        account = account?.copy(avatarUrl = imagePath)
                        if (account != null) viewModel.updateAccount(account!!)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUI()
        setListeners()
    }

    private fun setListeners() = with(binding) {
        btnBack.setOnClickListener { viewModel.returnBack() }
        switchTheme.setOnClickListener { handleThemeClick() }
        switchNoInternet.setOnClickListener { handleNoInternetWarningClick() }
        exitButton.setOnClickListener { handleExitClick() }
        deleteButton.setOnClickListener { viewModel.removeAccount() }
        changeAccountPhotoButton.setOnClickListener { handleChangeAccountImageClick() }

        setOnBackPressListener()

        viewModel.deleteAccountsResult.observe(viewLifecycleOwner, ::handleDeleteResult)
        viewModel.readAccountResult.observe(viewLifecycleOwner, ::handleAccountReadingResult)
        viewModel.readLikedGifsCountResult.observe(viewLifecycleOwner) {
            handleGifsCountResult(binding.likedCountText, it)
        }
        viewModel.readSavedGifsCountResult.observe(viewLifecycleOwner) {
            handleGifsCountResult(binding.savedCountText, it)
        }
        viewModel.updateAccountResult.observe(viewLifecycleOwner, ::handleUpdateAccountResult)
    }

    private fun handleUpdateAccountResult(result: Result<Boolean>) {
        val value = result.getOrNull()
        if (value == null || !value) {
            requireContext().showToast(resources.getString(CoreResources.string.error))
            binding.accountImage.setImageResource(CoreResources.drawable.default_account_icon)
        }
    }

    private fun handleChangeAccountImageClick() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        pickAccountImageLauncher.launch(intent)
    }

    private fun handleAccountReadingResult(result: Result<AccountEntity>) {
        val value = result.getOrNull()
        if (value == null) {
            requireContext().showToast(resources.getString(R.string.account_was_not_found))
            return
        }

        account = value
        Glide.with(requireContext())
            .load(value.avatarUrl)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .error(CoreResources.drawable.default_account_icon)
            .into(binding.accountImage)
    }

    private fun setOnBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.returnBack()
        }
    }

    private fun handleGifsCountResult(textView: TextView, result: Result<Int>) {
        val value = result.getOrNull()
        if (value == null) textView.text = resources.getString(CoreResources.string.error)
        else textView.text = value.toString()
    }

    private fun handleDeleteResult(result: Result<Boolean>) {
        val isDeleted = result.getOrNull()
        if (isDeleted == null) {
            requireContext().showToast(resources.getString(CoreResources.string.error))
        } else if (isDeleted) {
            handleExitClick()
        }
    }

    private fun handleExitClick() {
        requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        ).setDefaultLoginValues()
        viewModel.exitFromAccount()
    }

    private fun handleThemeClick() {
        val sharedPreferences = requireContext().getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        sharedPreferences.edit()
            .putInt(APP_THEME, if (binding.switchTheme.isChecked) 0 else 1)
            .apply()

        requireActivity().recreate()
    }

    private fun handleNoInternetWarningClick() {
        val sharedPreferences = requireContext().getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        sharedPreferences.edit()
            .putBoolean(SHOW_NETWORK_WARNING, binding.switchNoInternet.isChecked)
            .apply()
    }

    private fun loadUI() {
        if (themeIdentifier.isLightTheme) setLightTheme() else setDarkTheme()

        loadThemeState()
        loadNoNetworkWarningState()
    }

    private fun loadNoNetworkWarningState() {
        val sharedPreferences = requireContext().getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val showNoNetworkWarningState = sharedPreferences.getBoolean(SHOW_NETWORK_WARNING, true)
        binding.switchNoInternet.isChecked = showNoNetworkWarningState
    }

    private fun loadThemeState() {
        val sharedPreferences = requireContext().getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val themeState = sharedPreferences.getInt(APP_THEME, -1)
        if (themeState == 0) {
            binding.switchTheme.isChecked = true
        }
    }

    private fun setLightTheme() = with(binding) {
        setCardLightTheme(designCard)
        setCardLightTheme(infoCard)
        setCardLightTheme(accountManagerCard)
        btnBack.setColorFilter(Color.BLACK)
    }

    private fun setCardLightTheme(card: MaterialCardView) {
        card.elevation = 0f
        card.strokeWidth = 0
    }

    private fun setDarkTheme() = with(binding) {
        btnBack.setColorFilter(Color.WHITE)
    }
}
