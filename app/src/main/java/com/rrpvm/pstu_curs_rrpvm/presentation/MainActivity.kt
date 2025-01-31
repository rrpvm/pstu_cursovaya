package com.rrpvm.pstu_curs_rrpvm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.rrpvm.core.presentation.fadeIn
import com.rrpvm.core.presentation.fadeOff
import com.rrpvm.pstu_curs_rrpvm.AppNavDirections
import com.rrpvm.pstu_curs_rrpvm.R
import com.rrpvm.pstu_curs_rrpvm.databinding.ActivityMainBinding
import com.rrpvm.pstu_curs_rrpvm.presentation.navigation.KinoZBottomNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val ANIM_TIME_SPLASH = 700L

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_activity_fragment_host) as NavHostFragment
        val navController = navHostFragment.navController

        KinoZBottomNavigation.setupWithNavController(
            binding.mainActivityFragmentHostBottomNavigation,
            navController
        )
        observeLoadingState()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuth.collectLatest { authenticated ->
                    if (authenticated) {
                        navController.navigate(AppNavDirections.actionGoFeed())
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isShowActivityProgress.distinctUntilChangedBy { it }
                    .onEach { isProgress ->
                        binding.apply {
                            if (isProgress) {
                                with(llActivityContent) {
                                    clearAnimation()//android view
                                    isVisible = false
                                }
                                with(lavActivityProgressLoader) {
                                    clearAnimation()//android view
                                    fadeIn(ANIM_TIME_SPLASH)
                                    playAnimation()//lottie
                                }
                            } else {
                                with(llActivityContent) {
                                    clearAnimation()//android view
                                    fadeIn(ANIM_TIME_SPLASH)
                                }
                                with(lavActivityProgressLoader) {
                                    clearAnimation()//android view
                                    fadeOff(ANIM_TIME_SPLASH) {
                                        pauseAnimation()//lottie
                                        this.isVisible = false
                                    }
                                }
                            }
                        }

                    }.launchIn(this)
            }
        }
    }


}