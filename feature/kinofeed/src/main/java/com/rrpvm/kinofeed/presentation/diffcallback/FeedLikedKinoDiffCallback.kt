package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.domain.model.kino.listsEqual
import com.rrpvm.kinofeed.presentation.model.LikedKinoFeedItem


object FeedLikedKinoDiffCallback : DiffUtil.ItemCallback<LikedKinoFeedItem>() {
    override fun areItemsTheSame(oldItem: LikedKinoFeedItem, newItem: LikedKinoFeedItem): Boolean {
        return true
    }

    override fun areContentsTheSame(
        oldItem: LikedKinoFeedItem,
        newItem: LikedKinoFeedItem
    ): Boolean {
        return listsEqual(oldItem.likedKinoList, newItem.likedKinoList)
    }
}