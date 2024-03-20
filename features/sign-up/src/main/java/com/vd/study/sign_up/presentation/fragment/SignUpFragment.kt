package com.vd.study.sign_up.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.image.getDefaultAccountDrawableUrl
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.utils.USERNAME_REGEX_STRING
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.sign_up.R
import com.vd.study.core.R as CoreResources
import com.vd.study.sign_up.databinding.FragmentSignUpBinding
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var isProgress = false

    private val emailPattern = Patterns.EMAIL_ADDRESS

    private val viewModel: SignUpViewModel by viewModels()

    private val binding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hideBottomBar()
        Glide.with(requireContext())
            .load(CoreResources.drawable.default_account_icon)
            .into(binding.imageAccount)

        binding.btnNext.setOnClickListener { handleNextClick() }
        binding.btnBack.setOnClickListener { handleBackClick() }
        binding.btnRegister.setOnClickListener { handleRegisterClick() }

        binding.usernameEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isUsernameInputCorrect(), binding.usernameEditText, hasFocus)
        }
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isEmailInputCorrect(), binding.emailEditText, hasFocus)
        }
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            textChangedListenerHandler(isPasswordInputCorrect(), binding.passwordEditText, hasFocus)
        }

        viewModel.registrationResultLiveValue.observe(viewLifecycleOwner) {
            handleRegistrationResult(it)
        }
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
                result.getOrNull()
                    ?: requireContext().showToast(resources.getString(CoreResources.string.error_try_again_later))
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
            editText.error = if (!matchResult) resources.getString(CoreResources.string.incorrect) else null
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
        viewModel.registerAccount(account)
    }

    // add handler
    // comparing is not work
    private fun getAccountIconUrl(): String {
        val defaultImageFromView = binding.imageAccount.drawable
        val imageFromResources = ContextCompat.getDrawable(requireContext(), CoreResources.drawable.default_account_icon)
        if (defaultImageFromView.equals(imageFromResources)) {
            Log.i("MYTAG", "HEHEHEHEHEHHEHEHEHHEHEHEHEHHEHE")
            return requireContext().getDefaultAccountDrawableUrl()
        }

        Log.i("MYTAG", "------------------------------------")

        return requireContext().getDefaultAccountDrawableUrl()
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
        imageCard.isVisible = isImageAccountVisible
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
