package com.vd.study.gif_master.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.vd.study.core.global.ACCOUNT_ID_FIELD_NAME
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.gif_master.R
import com.vd.study.gif_master.databinding.ActivityMainBinding
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    @Inject
    lateinit var accountIdentifier: AccountIdentifier

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        globalNavComponentRouter.onCreated(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        setAccountIdentifier()

        binding.bottomAppBar.setOnMenuItemClickListener {
            handleOnBottomMenuItemClick(it, navController)
        }
        binding.searchButton.setOnClickListener {

        }
    }

    override fun onDestroy() {
        globalNavComponentRouter.onDestroyed()
        super.onDestroy()
    }

    private fun setAccountIdentifier() {
        val sharedPreferences = this.getSharedPreferences(
            SIGN_IN_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )

        val accountId = sharedPreferences.getInt(ACCOUNT_ID_FIELD_NAME, -1)
        if (accountId == -1) {
            // add handler
            return
        }
        accountIdentifier.accountIdentifier = accountId
    }

    private fun handleOnBottomMenuItemClick(item: MenuItem, navController: NavController): Boolean {
        return when (item.itemId) {
            R.id.homeFragment -> {
                if (navController.currentDestination?.id != R.id.homeFragment) {
                    navController.popBackStack(R.id.homeFragment, false)
                    true
                } else false
            }

            R.id.accountFragment -> {
                if (navController.currentDestination?.id != R.id.accountFragment) {
                    navController.popBackStack(R.id.homeFragment, false)
                    navController.navigate(R.id.accountFragment)
                    true
                } else false
            }

            else -> false
        }
    }
}
