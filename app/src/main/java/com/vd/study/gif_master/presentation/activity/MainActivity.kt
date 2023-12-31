package com.vd.study.gif_master.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.vd.study.gif_master.R
import com.vd.study.gif_master.databinding.ActivityMainBinding
import com.vd.study.gif_master.presentation.router.GlobalNavComponentRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var globalNavComponentRouter: GlobalNavComponentRouter

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        globalNavComponentRouter.onCreated(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        binding.bottomNavBar.setupWithNavController(navHostFragment.navController)
    }

    override fun onDestroy() {
        globalNavComponentRouter.onDestroyed()
        super.onDestroy()
    }
}