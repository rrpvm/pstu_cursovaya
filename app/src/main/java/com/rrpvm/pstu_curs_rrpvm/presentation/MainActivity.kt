package com.rrpvm.pstu_curs_rrpvm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.rrpvm.pstu_curs_rrpvm.AppNavDirections
import com.rrpvm.pstu_curs_rrpvm.R
import com.rrpvm.pstu_curs_rrpvm.databinding.ActivityMainBinding
import com.rrpvm.pstu_curs_rrpvm.presentation.navigation.KinoZBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_fragment_host) as NavHostFragment
        val navController = navHostFragment.navController

        KinoZBottomNavigation.setupWithNavController(
            binding.mainActivityFragmentHostBottomNavigation,
            navController
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuth.collectLatest { authenticated ->
                    if (authenticated) {
                        navController.navigate(AppNavDirections.actionGoProfile())
                    } else {
                        if (navController.currentDestination?.parent?.id != R.id.app_nav &&
                            navController.currentDestination?.parent?.id != com.rrpvm.authorization.R.id.auth_graph
                        ) {
                            navController.navigate(AppNavDirections.actionGoSplashScreen())
                        }
                    }
                }
            }
        }
    }
}