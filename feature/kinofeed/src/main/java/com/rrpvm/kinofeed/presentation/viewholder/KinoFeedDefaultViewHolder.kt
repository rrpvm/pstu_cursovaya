package com.rrpvm.kinofeed.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rrpvm.kinofeed.presentation.model.FeedItemUi

abstract class KinoFeedDefaultViewHolder(view : View) : ViewHolder(view) {
    abstract fun onBind(item : FeedItemUi)
    abstract fun onViewRecycled()
}