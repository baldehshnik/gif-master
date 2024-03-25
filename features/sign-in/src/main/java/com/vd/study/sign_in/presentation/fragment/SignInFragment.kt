package com.vd.study.sign_in.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.vd.study.core.container.Result
import com.vd.study.core.global.ACCOUNT_EMAIL_FIELD_NAME
import com.vd.study.core.global.ACCOUNT_ID_FIELD_NAME
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.core.global.IS_ACCOUNT_ENTERED_FIELD_NAME
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.utils.PASSWORD_REGEX_STRING
import com.vd.study.core.presentation.utils.setDarkTheme
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.sign_in.R
import com.vd.study.sign_in.databinding.FragmentSignInBinding
import com.vd.study.sign_in.domain.entities.AccountEntity
import com.vd.study.sign_in.domain.entities.CheckAccountEntity
import com.vd.study.sign_in.presentation.adapter.OnRegisteredAccountClickListener
import com.vd.study.sign_in.presentation.adapter.RegisteredAccountAdapter
import com.vd.study.sign_in.presentation.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    @Inject
    lateinit var accountIdentifier: AccountIdentifier

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private val binding: FragmentSignInBinding by viewBinding()

    private val viewModel by viewModels<SignInViewModel>()

    private val onRegisteredAccountClickListener = object : OnRegisteredAccountClickListener {
        override fun onClick(account: AccountEntity) {
            signIn(account.id, account.email)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences = requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val isSaved = sharedPreferences.getBoolean(IS_ACCOUNT_ENTERED_FIELD_NAME, false)
        if (isSaved) viewModel.navigateToMain()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventsListener()
        readRegisteredAccounts()
        initUI()

        viewModel.hideBottomBar()

        binding.listSavedAccounts.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        binding.btnSignIn.setOnClickListener { handleOnSignInClick() }

        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isEmailInputCorrect()) {
                setEditTextErrorMessage(binding.emailEditText, CoreResources.string.incorrect)
            }
        }
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isPasswordInputCorrect()) {
                setEditTextErrorMessage(binding.passwordEditText, CoreResources.string.incorrect)
            }
        }

        binding.btnSignUp.setOnClickListener { viewModel.navigateToRegistration() }
        binding.btnReloadAccountsList.setOnClickListener { handleReloadClick() }
    }

    private fun initUI() {
        if (themeIdentifier.isLightTheme) {
            binding.btnSignIn.setTextColor(Color.WHITE)
            binding.btnSignUp.setTextColor(Color.BLACK)
        } else {
            binding.textSelectFrom.setTextColor(Color.WHITE)
            binding.textOrFill.setTextColor(Color.WHITE)
            binding.btnSignUp.setTextColor(Color.WHITE)
            binding.btnSignIn.setDarkTheme()
            binding.emailEditTextLayout.setDarkTheme(binding.emailEditText)
            binding.passwordEditTextLayout.setDarkTheme(binding.passwordEditText)
        }
    }

    private fun handleReloadClick() {
        binding.btnReloadAccountsList.isVisible = false
        changeAccountsListVisibility(true)
        viewModel.readRegisteredAccounts()
    }

    private fun readRegisteredAccounts() {
        viewModel.readRegisteredAccounts()
        changeAccountsListVisibility(true)
    }

    private fun signIn(id: Int, email: String) {
        val sharedPreferences = requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        sharedPreferences.edit()
            .putBoolean(IS_ACCOUNT_ENTERED_FIELD_NAME, true)
            .putString(ACCOUNT_EMAIL_FIELD_NAME, email)
            .putInt(ACCOUNT_ID_FIELD_NAME, id)
            .apply()

        accountIdentifier.accountIdentifier = id
        viewModel.navigateToMain()
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

    private fun handleSignInResult(result: Result<Int>) {
        when (result) {
            Result.Progress -> {
                binding.btnSignIn.isEnabled = false
            }

            is Result.Error -> {
                binding.btnSignIn.isEnabled = true
                requireContext().showToast(resources.getString(CoreResources.string.error_try_again_later))
            }

            is Result.Correct -> {
                signIn(result.getOrNull()!!, binding.emailEditText.text.toString())
                binding.btnSignIn.isEnabled = true
            }
        }
    }

    private fun handleRegisteredAccountsReading(result: Result<List<AccountEntity>>) {
        when (result) {
            Result.Progress -> {
                changeAccountsListVisibility(true)
            }

            is Result.Error -> {
                changeAccountsListVisibility(false)
                binding.btnReloadAccountsList.isVisible = true
            }

            is Result.Correct -> {
                changeAccountsListVisibility(false)
                correctAccountsReading(result.getOrNull())
            }
        }
    }

    private fun changeAccountsListVisibility(inProgress: Boolean) = with(binding) {
        listSavedAccounts.isInvisible = inProgress
        accountsProgressBar.isVisible = inProgress
    }

    private fun handleOnSignInClick() {
        if (!isEmailInputCorrect()) {
            setEditTextErrorMessage(binding.emailEditText, CoreResources.string.incorrect)
            return
        }
        if (!isPasswordInputCorrect()) {
            setEditTextErrorMessage(binding.passwordEditText, CoreResources.string.incorrect)
            return
        }

        binding.btnSignIn.isEnabled = false
        val checkAccount = CheckAccountEntity(
            binding.emailEditText.text!!.toString(),
            binding.passwordEditText.text!!.toString()
        )
        viewModel.singIn(checkAccount)
    }

    private fun correctAccountsReading(accounts: List<AccountEntity>?) {
        if (accounts == null) {
            return
        }

        val adapter = RegisteredAccountAdapter(accounts, onRegisteredAccountClickListener, themeIdentifier.isLightTheme)
        binding.listSavedAccounts.adapter = adapter
        viewModel.hideProgress()
    }
}
