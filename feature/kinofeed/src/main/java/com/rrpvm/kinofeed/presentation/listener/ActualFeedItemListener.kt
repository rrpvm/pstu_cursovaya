package com.rrpvm.kinofeed.presentation.listener

interface ActualFeedItemListener {
    fun onShiftLeft() : Unit
    fun onShiftRight() : Unit
    fun onKinoSelected(kinoId:String) : Unit
}