package com.rrpvm.profile.presentation.menu

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.core.toPx
import com.rrpvm.profile.presentation.model.ProfileMenuItem
import com.rrpvm.profile.presentation.menu.viewholder.ClientInfoViewHolder
import com.rrpvm.profile.presentation.menu.viewholder.LogoutButtonViewHolder

class ProfileMenuDecorator(
    private val _strokeWidth: Float,
    @ColorInt private val _strokeColor: Int,
    private val footerMargin: Int
) :
    RecyclerView.ItemDecoration() {
    private val painter = Paint().apply {
        strokeWidth = _strokeWidth
        style = Paint.Style.STROKE
        color = _strokeColor
        isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (child in parent.children) {
            val viewholder = parent.findContainingViewHolder(child)
            if (viewholder is ClientInfoViewHolder || viewholder is LogoutButtonViewHolder) {
                continue
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
        val viewHolder = parent.findContainingViewHolder(view)
        if (viewHolder?.itemViewType == ProfileMenuItem.LogoutButtonItem.VIEW_TYPE) {
            if (view.height == 0) {
                val widthSpec =
                    View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
                val heightSpec =
                    View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
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
        if (viewHolder?.itemViewType == ProfileMenuItem.ClientItem.VIEW_TYPE) {
            val margin8 = view.resources.toPx(8F).toInt()
            outRect.left = margin8
            outRect.right = margin8
            outRect.bottom = margin8
            return
        }
        outRect.bottom = (_strokeWidth).toInt()
        //outRect.left = view.resources.toPx(8F).toInt()
        // outRect.right = view.resources.toPx(8F).toInt()
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
        if (remainingSpace < 0) {
            outRect.top = footerMargin
            outRect.bottom = footerMargin
            return
        }
        if (remainingSpace > view.measuredHeight + footerMargin) {
            outRect.top = remainingSpace - view.height - footerMargin
        } else {
            outRect.top = footerMargin
            outRect.bottom = footerMargin
        }
    }

}