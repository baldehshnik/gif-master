package com.vd.study.settings.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vd.study.core.presentation.toast.showToast
import com.vd.study.core.presentation.viewbinding.viewBinding
import com.vd.study.settings.R
import com.vd.study.settings.databinding.FragmentSettingsBinding
import com.vd.study.settings.domain.entities.AccountEntity
import com.vd.study.settings.presentation.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var account: AccountEntity? = null

    @Inject
    lateinit var factory: SettingsViewModel.Factory

    private val binding: FragmentSettingsBinding by viewBinding()

    private val _viewModel: Lazy<SettingsViewModel> by lazy {
        requireNotNull(account) { resources.getString(R.string.account_was_not_initialized) }
        viewModels { SettingsViewModel.provideFactory(factory, account!!) }
    }
    private val viewModel: SettingsViewModel get() = _viewModel.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = getAccount()
        if (account == null) {
            requireContext().showToast(resources.getString(R.string.account_was_not_initialized))
            findNavController().popBackStack()
        } else {
            this.account = account
            loadUI(account)
        }
    }

    private fun loadUI(account: AccountEntity) {

    }

    private fun getAccount(): AccountEntity? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(INCOMING_ACCOUNT, AccountEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(INCOMING_ACCOUNT)
        }
    }

    companion object {
        const val INCOMING_ACCOUNT = "account"
    }
}