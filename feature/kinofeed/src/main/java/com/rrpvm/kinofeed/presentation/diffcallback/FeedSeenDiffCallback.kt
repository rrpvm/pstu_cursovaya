package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.domain.model.kino.listsEqual

import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem

object FeedSeenDiffCallback : DiffUtil.ItemCallback<SeenKinoFeedItem>() {
    override fun areItemsTheSame(oldItem: SeenKinoFeedItem, newItem: SeenKinoFeedItem): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: SeenKinoFeedItem, newItem: SeenKinoFeedItem): Boolean {
        return oldItem.viewedKinoList.size == newItem.viewedKinoList.size
                && listsEqual(oldItem.viewedKinoList, oldItem.viewedKinoList)
    }
}