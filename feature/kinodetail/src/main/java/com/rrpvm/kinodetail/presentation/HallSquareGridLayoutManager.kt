package com.rrpvm.kinodetail.presentation

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HallSquareGridLayoutManager(
    context: Context,
    private val spanCount: Int
) : GridLayoutManager(context, spanCount) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        // Рассчитываем высоту элементов, основываясь на ширине
        val totalWidth = width - paddingLeft - paddingRight
        val itemWidth = totalWidth / spanCount
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val params = child.layoutParams
            params.height = calculateDynamicHeight(itemWidth, child.measuredHeight, spanCount)
            child.layoutParams = params
        }
    }

    private fun calculateDynamicHeight(itemWidth: Int, measuredHeight: Int, spanCount: Int): Int {
        if (spanCount < 9 || itemWidth < measuredHeight) {
            return ViewGroup.LayoutParams.WRAP_CONTENT
        }
        return itemWidth
    }
}