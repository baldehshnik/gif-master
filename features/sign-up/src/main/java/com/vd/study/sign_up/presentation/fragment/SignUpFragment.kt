package com.vd.study.sign_up.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vd.study.core.container.Result
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.image.getDefaultAccountDrawableUrl
import com.vd.study.core.presentation.image.saveImageToInternalStorage
import com.vd.study.core.presentation.regex.isEmailInputCorrect
import com.vd.study.core.presentation.regex.isPasswordInputCorrect
import com.vd.study.core.presentation.regex.isUsernameInputCorrect
import com.vd.study.core.presentation.resources.getColorValue
import com.vd.study.core.presentation.resources.getDrawableValue
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.utils.ACCOUNT_IMAGE_FILE_NAME
import com.vd.study.core.presentation.utils.setDarkTheme
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.sign_up.R
import com.vd.study.sign_up.databinding.FragmentSignUpBinding
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var isProgress = false

    private var imageFilePath: String? = null

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private val viewModel: SignUpViewModel by viewModels()

    private val binding: FragmentSignUpBinding by viewBinding()

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                binding.imageAccount.setImageURI(uri)
                requireContext().saveImageToInternalStorage(
                    uri, ACCOUNT_IMAGE_FILE_NAME,
                    resultHandler = { imageFilePath = it },
                    errorHandler = ::errorImageSelection
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()

        viewModel.hideBottomBar()
        viewModel.registrationResultLiveValue.observe(viewLifecycleOwner, ::handleRegistrationResult)
    }

    private fun handleAccountImageClick() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    private fun errorImageSelection() {
        requireContext().showToast(resources.getString(R.string.account_image_selection_error))
    }

    private fun initListeners() = with(binding) {
        btnNext.setOnClickListener { handleNextClick() }
        btnBack.setOnClickListener { handleBackClick() }
        btnRegister.setOnClickListener { handleRegisterClick() }
        imageAccount.setOnClickListener { handleAccountImageClick() }

        usernameEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isUsernameInputCorrect(usernameEditText), usernameEditText, hasFocus)
        }
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isEmailInputCorrect(emailEditText), emailEditText, hasFocus)
        }
        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isPasswordInputCorrect(passwordEditText), passwordEditText, hasFocus)
        }
    }

    private fun initUI() {
        Glide.with(requireContext())
            .load(CoreResources.drawable.default_account_icon)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .centerCrop()
            .into(binding.imageAccount)

        if (themeIdentifier.isLightTheme) setScreenLightTheme() else setScreenDarkTheme()
    }

    private fun setScreenDarkTheme() = with(binding) {
        btnBack.setColorFilter(Color.WHITE)
        btnNext.setDarkTheme()
        btnRegister.setDarkTheme()

        textRegistrationScreen.setTextColor(Color.WHITE)

        usernameEditTextLayout.apply {
            setDarkTheme(usernameEditText)
            applyDefaultStyleToEditText(this, usernameEditText)
        }
        emailEditTextLayout.apply {
            setDarkTheme(emailEditText)
            applyDefaultStyleToEditText(this, emailEditText)
        }
        passwordEditTextLayout.apply {
            setDarkTheme(passwordEditText)
            applyDefaultStyleToEditText(this, passwordEditText)
        }
    }

    private fun setScreenLightTheme() = with(binding) {
        btnNext.setTextColor(Color.WHITE)
        btnRegister.setTextColor(Color.WHITE)
        btnBack.setColorFilter(Color.BLACK)
    }

    private fun applyDefaultStyleToEditText(layout: TextInputLayout, editText: TextInputEditText) {
        layout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        editText.background = requireContext().getDrawableValue(R.drawable.edit_text_dark_theme_background)
        editText.setBackgroundColor(requireContext().getColorValue(CoreResources.color.second_background))
    }

    private fun handleRegistrationResult(result: Result<AccountEntity>) {
        when (result) {
            Result.Progress -> {
                changeRegistrationVisibility(true)
            }

            is Result.Error -> {
                changeRegistrationVisibility(false)
                requireContext().showToast(resources.getString(CoreResources.string.error_try_again_later))
            }

            is Result.Correct -> {
                changeRegistrationVisibility(false)
                result.getOrNull() ?: requireContext().showToast(
                    resources.getString(CoreResources.string.error_try_again_later)
                )

                viewModel.navigateToMain()
            }
        }
    }

    private fun changeRegistrationVisibility(isProgress: Boolean) {
        this.isProgress = isProgress
        binding.registrationProgress.isVisible = isProgress
        binding.btnRegister.isClickable = !isProgress
    }

    private fun textChangedListenerHandler(
        matchResult: Boolean, editText: TextInputEditText, hasFocus: Boolean
    ) {
        if (!hasFocus) editText.error = if (!matchResult) resources.getString(CoreResources.string.incorrect) else null
    }

    private fun handleRegisterClick() {
        if (isProgress) return
        val account = AccountEntity(
            username = binding.usernameEditText.text.toString(),
            avatarUrl = getAccountIconUrl(),
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString(),
            date = Date()
        )

        changeRegistrationVisibility(true)
        viewModel.registerAccount(account)
    }

    private fun getAccountIconUrl(): String {
        return imageFilePath ?: requireContext().getDefaultAccountDrawableUrl()
    }

    private fun handleBackClick() {
        if (binding.btnNext.isVisible) {
            viewModel.returnToSignIn()
            return
        }

        binding.imageAccount.setImageResource(CoreResources.drawable.default_account_icon)
        setImageSelectionScreenVisibility()
        setEditTextFieldsVisibility()
        changeRegistrationVisibility(false)
    }

    private fun handleNextClick() {
        var flag = true
        if (!isUsernameInputCorrect(binding.usernameEditText)) {
            setEditTextFieldsVisibility(isEmailFieldVisible = false, isPasswordFieldVisible = false)
            binding.usernameEditText.requestFocus()
            showEditTextError(binding.usernameEditText)
            return
        } else {
            if (!binding.emailEditTextLayout.isVisible) {
                setEditTextFieldsVisibility(isPasswordFieldVisible = false)
                flag = false
            }
        }

        if (!isEmailInputCorrect(binding.emailEditText)) {
            if (flag) showEditTextError(binding.emailEditText)

            setEditTextFieldsVisibility(isPasswordFieldVisible = false)
            binding.passwordEditText.text?.clear()
            binding.emailEditText.requestFocus()
            return
        } else {
            if (!binding.passwordEditTextLayout.isVisible) {
                setEditTextFieldsVisibility()
                flag = false
            }
        }

        if (!isPasswordInputCorrect(binding.passwordEditText)) {
            if (flag) showEditTextError(binding.passwordEditText)

            binding.passwordEditText.text?.clear()
            binding.passwordEditText.requestFocus()
        } else {
            setEditTextFieldsVisibility(
                isUsernameFieldVisible = false,
                isEmailFieldVisible = false,
                isPasswordFieldVisible = false
            )

            setImageSelectionScreenVisibility(
                isRegisterButtonVisible = true,
                isImageAccountVisible = true
            )
        }
    }

    private fun showEditTextError(editText: TextInputEditText) {
        editText.error = resources.getString(CoreResources.string.incorrect)
    }

    private fun setImageSelectionScreenVisibility(
        isRegisterButtonVisible: Boolean = false, isImageAccountVisible: Boolean = false
    ) = with(binding) {
        btnRegister.isVisible = isRegisterButtonVisible
        imageAccount.isVisible = isImageAccountVisible
    }

    private fun setEditTextFieldsVisibility(
        isUsernameFieldVisible: Boolean = true,
        isEmailFieldVisible: Boolean = true,
        isPasswordFieldVisible: Boolean = true
    ) = with(binding) {
        usernameEditTextLayout.isVisible = isUsernameFieldVisible
        emailEditTextLayout.isVisible = isEmailFieldVisible
        passwordEditTextLayout.isVisible = isPasswordFieldVisible
        btnNext.isVisible = isUsernameFieldVisible || isEmailFieldVisible || isPasswordFieldVisible
    }
}
