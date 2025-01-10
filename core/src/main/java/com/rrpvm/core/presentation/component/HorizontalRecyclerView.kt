package com.rrpvm.core.presentation.component

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class HorizontalRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    override fun canScrollVertically(direction: Int): Boolean {
        return false
    }
    /*
    override fun onTouchEvent(e: MotionEvent): Boolean {
       /* if (findChildViewUnder(e.x, e.y) == null) {
            parent?.requestDisallowInterceptTouchEvent(false)
            return false
        }*/
        return super.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
      //  val isEmptySpace = findChildViewUnder(e.x, e.y) == null
      /*  if (isEmptySpace) {
            parent?.requestDisallowInterceptTouchEvent(false)
            return false
        }*/
       // return e.action == MotionEvent.ACTION_MOVE
        return super.onInterceptTouchEvent(e)
    }*/
}