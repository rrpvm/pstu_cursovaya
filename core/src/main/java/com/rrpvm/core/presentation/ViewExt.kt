package com.rrpvm.core.presentation

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.core.view.isVisible

fun View.fadeIn(duration: Long) {
    val animation = AlphaAnimation(0.0F, 1F)
    animation.duration = duration
    animation.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            isVisible = true
        }

        override fun onAnimationEnd(animation: Animation?) {

        }

        override fun onAnimationRepeat(animation: Animation?) {

        }
    })
    this.animation = animation
    animation.start()
}

fun View.fadeOff(duration: Long, onEnd: View.() -> Unit) {
    val animation = AlphaAnimation(1F, 0F)
    animation.duration = duration
    animation.setAnimationListener(object : AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) {

        }
    })
    this.animation = animation
    animation.start()
}