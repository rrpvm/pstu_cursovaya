package com.rrpvm.pstu_curs_rrpvm.presentation.navigation

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavOptions
import com.google.android.material.navigation.NavigationBarView
import com.rrpvm.pstu_curs_rrpvm.AppNavDirections
import com.rrpvm.pstu_curs_rrpvm.R
import java.lang.ref.WeakReference

internal object KinoZBottomNavigation {
    fun setupWithNavController(
        navigationBarView: NavigationBarView,
        navController: NavController
    ) {
        navigationBarView.setOnItemSelectedListener { item ->
            onDestinationSelected(navController, item)
        }
        val weakReference = WeakReference(navigationBarView)
        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                val view = weakReference.get()
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }
                val isInAuthGraph = destination.hierarchy.any {
                    it.id == com.rrpvm.authorization.R.id.auth_graph
                }
                view.isVisible = isInAuthGraph.not()
                if (isInAuthGraph) {
                    return
                }
                view.menu.forEach { item ->
                    if (destination.hierarchy.any { it.id == item.itemId }) {
                        item.isChecked = true
                    }
                }
            }
        })

    }


    private fun onDestinationSelected(
        navController: NavController,
        viewItem: MenuItem
    ): Boolean {
        val builder = NavOptions.Builder().setLaunchSingleTop(true)
            .setPopUpTo(R.id.app_nav, true)
            .setRestoreState(true)
        //любые анимации
        val options = builder.build()
        return runCatching {
            when (viewItem.itemId) {
                R.id.btm_nav_feed -> {
                    navController.navigate(AppNavDirections.actionGoFeed(), options)
                }

                R.id.btm_nav_profile -> {
                    navController.navigate(AppNavDirections.actionGoProfile(), options)
                }

                else -> throw RuntimeException("unsupported menu item")
            }
            //isChecked
            navController.currentDestination
                ?.hierarchy
                ?.any {
                    it.id ==  when(viewItem.itemId){
                        R.id.btm_nav_feed-> com.rrpvm.kinofeed.R.id.feed_graph
                        R.id.btm_nav_profile-> com.rrpvm.profile.R.id.profile_graph
                        else->throw RuntimeException("unknown viewItem.itemId")
                    }
                } == true
        }.getOrDefault(false)
    }
}