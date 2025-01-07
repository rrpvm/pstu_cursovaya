package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem

object FeedSeenDiffCallback : DiffUtil.ItemCallback<SeenKinoFeedItem>() {
    override fun areItemsTheSame(oldItem: SeenKinoFeedItem, newItem: SeenKinoFeedItem): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: SeenKinoFeedItem, newItem: SeenKinoFeedItem): Boolean {
        return false
    }
}