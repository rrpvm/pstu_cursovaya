package com.rrpvm.core.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class HorizontalRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return e?.action == MotionEvent.ACTION_MOVE
    }
}