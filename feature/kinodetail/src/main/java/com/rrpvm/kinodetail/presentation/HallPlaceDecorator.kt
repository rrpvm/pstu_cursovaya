package com.rrpvm.kinodetail.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView


class HallPlaceDecorator(private val spanCount: Int, context: Context) :
    RecyclerView.ItemDecoration() {
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 1F
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (view in parent.children) {
            val rect = Rect()
            parent.getChildVisibleRect(view, rect, Point())
            c.drawRect(
                /* left = */
                view.x,
                /* top = */
                view.y,
                /* right = */
                view.x + view.width,
                /* bottom = */
                view.y + view.height,
                /* paint = */
                paint
            )
        }
    }
}