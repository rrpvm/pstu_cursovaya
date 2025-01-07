package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.NewsKinoFeedItem


object FeedNewsKinoDiffCallback : DiffUtil.ItemCallback<NewsKinoFeedItem>() {
    override fun areItemsTheSame(oldItem: NewsKinoFeedItem, newItem: NewsKinoFeedItem): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: NewsKinoFeedItem, newItem: NewsKinoFeedItem): Boolean {
        return false
    }
}