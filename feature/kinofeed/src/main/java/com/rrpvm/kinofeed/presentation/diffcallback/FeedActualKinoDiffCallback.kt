package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem


object FeedActualKinoDiffCallback : DiffUtil.ItemCallback<ActualKinoFeedItem>() {
    override fun areItemsTheSame(
        oldItem: ActualKinoFeedItem,
        newItem: ActualKinoFeedItem
    ): Boolean {
        return true
    }

    override fun areContentsTheSame(
        oldItem: ActualKinoFeedItem,
        newItem: ActualKinoFeedItem
    ): Boolean {
        return oldItem == newItem
    }
}