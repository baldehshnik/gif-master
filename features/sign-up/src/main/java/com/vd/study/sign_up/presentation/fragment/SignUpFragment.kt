package com.vd.study.sign_up.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.utils.ACCOUNT_IMAGE_FILE_NAME
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.utils.USERNAME_REGEX_STRING
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

    private val emailPattern = Patterns.EMAIL_ADDRESS

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
        viewModel.registrationResultLiveValue.observe(viewLifecycleOwner) {
            handleRegistrationResult(it)
        }
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

    private fun initListeners() {
        binding.btnNext.setOnClickListener { handleNextClick() }
        binding.btnBack.setOnClickListener { handleBackClick() }
        binding.btnRegister.setOnClickListener { handleRegisterClick() }
        binding.imageAccount.setOnClickListener { handleAccountImageClick() }

        binding.usernameEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isUsernameInputCorrect(), binding.usernameEditText, hasFocus)
        }
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isEmailInputCorrect(), binding.emailEditText, hasFocus)
        }
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isPasswordInputCorrect(), binding.passwordEditText, hasFocus)
        }
    }

    private fun initUI() {
        Glide.with(requireContext())
            .load(CoreResources.drawable.default_account_icon)
            .placeholder(CoreResources.drawable.placeholder_gray_gradient)
            .centerCrop()
            .into(binding.imageAccount)

        if (themeIdentifier.isLightTheme) {
            binding.btnNext.setTextColor(Color.WHITE)
            binding.btnRegister.setTextColor(Color.WHITE)
        } else {
            binding.textRegistrationScreen.setTextColor(Color.WHITE)
            binding.usernameEditTextLayout.apply {
                setDarkTheme(binding.usernameEditText)
                applyDefaultStyleToEditText(this, binding.usernameEditText)
            }
            binding.emailEditTextLayout.apply {
                setDarkTheme(binding.emailEditText)
                applyDefaultStyleToEditText(this, binding.emailEditText)
            }
            binding.passwordEditTextLayout.apply {
                setDarkTheme(binding.passwordEditText)
                applyDefaultStyleToEditText(this, binding.passwordEditText)
            }
            binding.btnNext.setDarkTheme()
            binding.btnRegister.setDarkTheme()
        }
    }

    private fun applyDefaultStyleToEditText(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ) {
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
        textInputEditText.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_dark_theme_background)
        textInputEditText.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                CoreResources.color.second_background
            )
        )
    }

    private fun handleRegistrationResult(result: Result<AccountEntity>) {
        when (result) {
            Result.Progress -> {
                changeRegistrationVisibility(true)
            }

            is Result.Error -> {
                binding.btnRegister.isClickable = true
                changeRegistrationVisibility(false)
                requireContext().showToast(resources.getString(CoreResources.string.error_try_again_later))
            }

            is Result.Correct -> {
                changeRegistrationVisibility(false)
                result.getOrNull() ?: requireContext().showToast(
                    resources.getString(CoreResources.string.error_try_again_later)
                )

                binding.btnRegister.isClickable = true
                viewModel.navigateToMain()
            }
        }
    }

    private fun changeRegistrationVisibility(isProgress: Boolean) = with(binding) {
        this@SignUpFragment.isProgress = isProgress
        registrationProgress.isVisible = isProgress
    }

    private fun isUsernameInputCorrect(): Boolean {
        return binding.usernameEditText.text?.toString()
            ?.matches(USERNAME_REGEX_STRING.toRegex()) != false
    }

    private fun isEmailInputCorrect(): Boolean {
        return emailPattern.matcher(binding.emailEditText.text?.toString() ?: "").matches()
    }

    private fun isPasswordInputCorrect(): Boolean {
        return binding.passwordEditText.text?.toString()
            ?.matches(PASSWORD_REGEX_STRING.toRegex()) != false
    }

    private fun textChangedListenerHandler(
        matchResult: Boolean,
        editText: TextInputEditText,
        hasFocus: Boolean
    ) {
        if (!hasFocus) {
            editText.error =
                if (!matchResult) resources.getString(CoreResources.string.incorrect) else null
        }
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

        binding.btnRegister.isClickable = false
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
        if (!isUsernameInputCorrect()) {
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

        if (!isEmailInputCorrect()) {
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

        if (!isPasswordInputCorrect()) {
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
        isRegisterButtonVisible: Boolean = false,
        isImageAccountVisible: Boolean = false
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
