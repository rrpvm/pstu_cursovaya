package com.rrpvm.pstu_curs_rrpvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.rrpvm.pstu_curs_rrpvm.databinding.ActivityMainBinding
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAuth.collectLatest { authenticated ->
                    if (authenticated) {
                        navController.clearBackStack(R.id.app_nav)
                        navController.navigate(R.id.action_go_application)
                    }
                    else {
                        navController.clearBackStack(R.id.app_nav)
                        navController.navigate(com.rrpvm.authorization.R.id.auth_graph)
                    }
                }
            }
        }
    }
}