package com.rrpvm.profile.presentation.menu

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem

class ProfileMenuDecorator(private val _strokeWidth: Float,private val footerMargin : Int) : RecyclerView.ItemDecoration() {
    private val painter = Paint().apply {
        strokeWidth = _strokeWidth
        style = Paint.Style.STROKE
        color = Color.valueOf(0.43F, 0.33f, 0.33f, 0.33f).toArgb()
        isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (child in parent.children) {
            if (parent.findContainingViewHolder(child)?.itemViewType == ProfileMenuItem.LogoutButtonItem.VIEW_TYPE) {
                return
            }
            val bounds: Rect = Rect()
            parent.getDecoratedBoundsWithMargins(child, bounds)
            c.drawLine(
                /* startX = */ bounds.left.toFloat(),
                /* startY = */
                bounds.bottom.toFloat(),
                /* stopX = */
                bounds.right.toFloat(),
                /* stopY = */
                bounds.bottom.toFloat(),
                /* paint = */
                painter
            )
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.findContainingViewHolder(view)?.itemViewType == ProfileMenuItem.LogoutButtonItem.VIEW_TYPE) {
            if(view.height == 0){
                val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
                val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
                val childWidth = ViewGroup.getChildMeasureSpec(
                    widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width
                )
                val childHeight = ViewGroup.getChildMeasureSpec(
                    heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height
                )
                view.measure(childWidth, childHeight)
                view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            }
            if (view.height != 0) {
                getItemFooterOffset(outRect, view, parent, state)
            }
            return
        }
        outRect.bottom = (_strokeWidth).toInt()
    }

    private fun getItemFooterOffset(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val myIndex = parent.getChildAdapterPosition(view)
        if (myIndex == 0) return
        val prevChild = parent.findViewHolderForPosition(myIndex - 1) ?: return
        val remainingSpace = parent.height - prevChild.itemView.bottom
        if (remainingSpace < 0){
            outRect.top = footerMargin
            outRect.bottom = footerMargin
            return
        }
        if (remainingSpace > view.measuredHeight + footerMargin) {
            outRect.top = remainingSpace - view.height - footerMargin
        }
        else{
            outRect.top = footerMargin
            outRect.bottom = footerMargin
        }
    }

}