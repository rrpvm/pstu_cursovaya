package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.FeedItemUi

class FeedItemUiDiffCallback : DiffUtil.ItemCallback<FeedItemUi>() {
    override fun areItemsTheSame(oldItem: FeedItemUi, newItem: FeedItemUi): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: FeedItemUi, newItem: FeedItemUi): Boolean {
        return true
    }
}