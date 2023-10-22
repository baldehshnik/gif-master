package com.vd.study.sign_in.presentation.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.vd.study.core.container.Result
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.sign_in.R
import com.vd.study.sign_in.databinding.FragmentSignInBinding
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.presentation.adapter.OnRegisteredAccountClickListener
import com.vd.study.sign_in.presentation.adapter.RegisteredAccountAdapter
import com.vd.study.sign_in.presentation.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding()

    private val viewModel by viewModels<SignInViewModel>()

    private val onRegisteredAccountClickListener = object : OnRegisteredAccountClickListener {
        override fun onClick(account: AccountEntity) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventsListener()

        binding.listSavedAccounts.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        binding.btnSignIn.setOnClickListener { handleOnSignInClick() }

        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isEmailInputCorrect()) {
                setEditTextErrorMessage(binding.emailEditText, R.string.incorrect)
            }
        }
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isPasswordInputCorrect()) {
                setEditTextErrorMessage(binding.passwordEditText, R.string.incorrect)
            }
        }
    }

    private fun setEventsListener() {
        viewModel.registeredAccountsLiveEvent.observe(viewLifecycleOwner) {
            handleRegisteredAccountsReading(it)
        }
        viewModel.checkAccountLiveEvent.observe(viewLifecycleOwner) {
            handleSignInResult(it)
        }
    }

    private fun isPasswordInputCorrect(): Boolean {
        return PASSWORD_REGEX_STRING.toRegex()
            .matches(binding.passwordEditText.text?.toString() ?: return false)
    }

    private fun isEmailInputCorrect(): Boolean {
        return Patterns.EMAIL_ADDRESS
            .matcher(binding.emailEditText.text?.toString() ?: return false)
            .matches()
    }

    private fun setEditTextErrorMessage(editText: TextInputEditText, @StringRes messageId: Int) {
        editText.error = resources.getString(messageId)
    }
    
    private fun handleSignInResult(result: Result<Boolean>) {
        when(result) {
            Result.Progress -> {

            }
            is Result.Error -> {

            }
            is Result.Correct -> {

            }
        }
    }

    private fun handleRegisteredAccountsReading(result: Result<List<AccountEntity>>) {
        when(result) {
            Result.Progress -> {

            }
            is Result.Error -> {

            }
            is Result.Correct -> correctAccountsReading(result.getOrNull())
        }
    }

    private fun handleOnSignInClick() {
        if (!isEmailInputCorrect()) {
            setEditTextErrorMessage(binding.emailEditText, R.string.incorrect)
            return
        }
        if (!isPasswordInputCorrect()) {
            setEditTextErrorMessage(binding.passwordEditText, R.string.incorrect)
            return
        }

        val checkAccount = CheckAccountEntity(
            email = binding.emailEditText.text!!.toString(),
            password = binding.passwordEditText.text!!.toString()
        )
        viewModel.singIn(checkAccount)
    }

    private fun correctAccountsReading(accounts: List<AccountEntity>?) {
        if (accounts == null) {
            return
        }

        val adapter = RegisteredAccountAdapter(accounts, onRegisteredAccountClickListener)
        binding.listSavedAccounts.adapter = adapter
        viewModel.hideProgress()
    }
}