package com.vd.study.sign_up.presentation.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.utils.USERNAME_REGEX_STRING
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.sign_up.R
import com.vd.study.sign_up.databinding.FragmentSignUpBinding
import com.vd.study.sign_up.domain.entities.AccountEntity
import com.vd.study.sign_up.presentation.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val emailPattern = Patterns.EMAIL_ADDRESS

    private val viewModel: SignUpViewModel by viewModels()

    private val binding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                // add
            }

            is Result.Error -> {
                // add
            }

            is Result.Correct -> {
                // fix
                val account = result.getOrNull()!!
                viewModel.navigateToMain()
            }
        }
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
            if (!matchResult) {
                editText.error = resources.getString(R.string.incorrect)
            } else {
                editText.error = null
            }
        }
    }

    private fun handleRegisterClick() {
        val account = AccountEntity(
            username = binding.usernameEditText.text?.toString() ?: "",
            avatarUrl = "url",
            email = binding.emailEditText.text?.toString() ?: "",
            password = binding.passwordEditText.text?.toString() ?: ""
        )
        viewModel.registerAccount(account)
    }

    private fun handleBackClick() {
        if (binding.btnNext.isVisible) {
            viewModel.returnToSignIn()
            return
        }

        binding.imageAccount.setImageResource(0)
        setImageSelectionScreenVisibility()
        setEditTextFieldsVisibility()
    }

    private fun handleNextClick() {
        if (!isUsernameInputCorrect()) {
            setEditTextFieldsVisibility(isEmailFieldVisible = false, isPasswordFieldVisible = false)
            binding.usernameEditText.requestFocus()
            return
        } else {
            setEditTextFieldsVisibility(isPasswordFieldVisible = false)
        }

        if (!isEmailInputCorrect()) {
            setEditTextFieldsVisibility(isPasswordFieldVisible = false)
            binding.passwordEditText.text?.clear()
            binding.emailEditText.requestFocus()
            return
        } else {
            setEditTextFieldsVisibility()
        }

        if (!isPasswordInputCorrect()) {
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
    }
}