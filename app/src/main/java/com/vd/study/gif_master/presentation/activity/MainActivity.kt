package com.vd.study.gif_master.presentation.activity

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.vd.study.core.global.ACCOUNT_ID_FIELD_NAME
import com.vd.study.core.global.APP_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.APP_THEME
import com.vd.study.core.global.AccountIdentifier
import com.vd.study.core.global.SHOW_NETWORK_WARNING
import com.vd.study.core.global.SIGN_IN_SHARED_PREFERENCES_NAME
import com.vd.study.core.global.ThemeIdentifier
import com.vd.study.core.presentation.dialog.showNetworkWarningDialog
import com.vd.study.gif_master.MainGraphDirections
import com.vd.study.gif_master.R
import com.vd.study.gif_master.databinding.ActivityMainBinding
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.vd.study.core.R as CoreResources

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    @Inject
    lateinit var accountIdentifier: AccountIdentifier

    @Inject
    lateinit var themeIdentifier: ThemeIdentifier

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val connectivityManager =
        lazy { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            handleNetworkDisabling()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadAndSetAppTheme()
        setContentView(binding.root)
        globalNavComponentRouter.onCreated(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        setAccountIdentifier()

        binding.bottomAppBar.setOnMenuItemClickListener {
            handleOnBottomMenuItemClick(it, navController)
        }
        binding.searchButton.setOnClickListener {
            globalNavComponentRouter.launch(MainGraphDirections.actionGlobalSearchFragment())
            changeBottomBarVisibility(false)
        }

        connectivityManager.value.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onDestroy() {
        connectivityManager.value.unregisterNetworkCallback(networkCallback)
        globalNavComponentRouter.onDestroyed()
        super.onDestroy()
    }

    fun changeBottomBarVisibility(isVisible: Boolean) {
        binding.bottomAppBar.isVisible = isVisible
        binding.searchButton.isVisible = isVisible
    }

    fun changeBottomBarTheme(default: Boolean) {
        if (themeIdentifier.isLightTheme) {
            val bottomBarColor =
                if (default) CoreResources.color.white else CoreResources.color.second_background
            val itemsColor = if (default) Color.BLACK else Color.WHITE

            setBottomBarColor(bottomBarColor)
            setBottomBarItemsColor(itemsColor)
            setBottomBarButtonIconColor(Color.WHITE)
        } else {
            val bottomBarColor = CoreResources.color.second_background
            val itemsColor = Color.WHITE

            setBottomBarColor(bottomBarColor)
            setBottomBarItemsColor(itemsColor)
            setBottomBarButtonIconColor(Color.BLACK)
        }
    }

    private fun handleNetworkDisabling() {
        val sharedPreferences = getSharedPreferences(
            APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )
        val showNetworkWarning = sharedPreferences.getBoolean(SHOW_NETWORK_WARNING, true)
        if (showNetworkWarning) showNetworkWarningDialog(themeIdentifier)
    }

    private fun setBottomBarButtonIconColor(color: Int) {
        binding.searchButton.setColorFilter(color)
    }

    private fun setBottomBarItemsColor(color: Int) {
        val menu = binding.bottomAppBar.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.icon?.setTint(color)
        }
    }

    private fun setBottomBarColor(color: Int) {
        binding.bottomAppBar.backgroundTint = ContextCompat.getColorStateList(
            applicationContext,
            color
        )
    }

    private fun loadAndSetAppTheme() {
        val sharedPreferences =
            getSharedPreferences(APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val loadTheme = {
            setTheme(
                if (themeIdentifier.isLightTheme) CoreResources.style.Theme_Gifmaster_Core
                else CoreResources.style.Theme_Gifmaster_Core_Dark
            )
        }

        when (sharedPreferences.getInt(APP_THEME, -1)) {
            -1 -> {
                themeIdentifier.isLightTheme = true
                loadTheme()
                sharedPreferences.edit()
                    .putInt(APP_THEME, 1)
                    .apply()
            }

            0 -> {
                themeIdentifier.isLightTheme = false
                loadTheme()
            }

            1 -> {
                themeIdentifier.isLightTheme = true
                loadTheme()
            }
        }

        changeBottomBarTheme(true)
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
                    navController.navigate(R.id.action_homeFragment_to_accountFragment)
                    true
                } else false
            }

            else -> false
        }
    }
}
