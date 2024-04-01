package com.vd.study.sign_in.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
import com.vd.study.core.presentation.regex.isEmailInputCorrect
import com.vd.study.core.presentation.regex.isPasswordInputCorrect
import com.vd.study.core.presentation.toast.showToast
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        checkUserLogin()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEventsListener()
        readRegisteredAccounts()
        initUI()
    }

    private fun checkUserLogin() {
        val sharedPreferences = requireContext().getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val isSaved = sharedPreferences.getBoolean(IS_ACCOUNT_ENTERED_FIELD_NAME, false)
        if (isSaved) viewModel.navigateToMain()
    }

    private fun initUI() {
        viewModel.hideBottomBar()
        if (themeIdentifier.isLightTheme) setLightThemeElements() else setDarkThemeElements()

        setListeners()
        binding.listSavedAccounts.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
    }

    private fun setLightThemeElements() = with(binding) {
        btnSignIn.setTextColor(Color.WHITE)
        btnSignUp.setTextColor(Color.BLACK)
    }

    private fun setDarkThemeElements() = with(binding) {
        textSelectFrom.setTextColor(Color.WHITE)
        textOrFill.setTextColor(Color.WHITE)

        btnSignUp.setTextColor(Color.WHITE)
        btnSignIn.setDarkTheme()

        emailEditTextLayout.setDarkTheme(emailEditText)
        passwordEditTextLayout.setDarkTheme(passwordEditText)
    }

    private fun setListeners() = with(binding) {
        btnSignIn.setOnClickListener { handleOnSignInClick() }
        btnSignUp.setOnClickListener { viewModel.navigateToRegistration() }
        btnReloadAccountsList.setOnClickListener { handleReloadClick() }

        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isEmailInputCorrect(emailEditText)) {
                setEditTextErrorMessage(emailEditText, CoreResources.string.incorrect)
            }
        }
        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && !isPasswordInputCorrect(passwordEditText)) {
                setEditTextErrorMessage(passwordEditText, CoreResources.string.incorrect)
            }
        }
    }

    private fun handleReloadClick() {
        binding.btnReloadAccountsList.isVisible = false
        readRegisteredAccounts()
    }

    private fun readRegisteredAccounts() {
        changeAccountsListVisibility(true)
        viewModel.readRegisteredAccounts()
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
        viewModel.registeredAccountsLiveEvent.observe(viewLifecycleOwner, ::handleRegisteredAccountsReading)
        viewModel.checkAccountLiveEvent.observe(viewLifecycleOwner, ::handleSignInResult)
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
        if (!isEmailInputCorrect(binding.emailEditText)) {
            setEditTextErrorMessage(binding.emailEditText, CoreResources.string.incorrect)
            return
        }
        if (!isPasswordInputCorrect(binding.passwordEditText)) {
            setEditTextErrorMessage(binding.passwordEditText, CoreResources.string.incorrect)
            return
        }

        binding.btnSignIn.isEnabled = false
        val checkAccount = CheckAccountEntity(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
        viewModel.singIn(checkAccount)
    }

    private fun correctAccountsReading(accounts: List<AccountEntity>?) {
        if (accounts == null) {
            return
        }

        val adapter = RegisteredAccountAdapter(accounts, onRegisteredAccountClickListener, themeIdentifier.isLightTheme)
        binding.listSavedAccounts.adapter = adapter
    }
}
