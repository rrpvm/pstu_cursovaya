package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.NewsKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem


class FeedItemUiDiffCallback : DiffUtil.ItemCallback<FeedItemUi>() {
    override fun areItemsTheSame(oldItem: FeedItemUi, newItem: FeedItemUi): Boolean {
        if (oldItem::class.java != newItem::class.java || oldItem::class != newItem::class) return false
        return when (oldItem::class) {
            SeenKinoFeedItem::class -> areItemsTheSame(oldItem, newItem, FeedSeenDiffCallback)
            ActualKinoFeedItem::class -> areItemsTheSame(
                oldItem,
                newItem,
                FeedActualKinoDiffCallback
            )

            NewsKinoFeedItem::class -> areItemsTheSame(
                oldItem,
                newItem,
                FeedNewsKinoDiffCallback
            )

            else -> throw IllegalArgumentException("no supported Kotlin class")

        }
    }

    override fun areContentsTheSame(oldItem: FeedItemUi, newItem: FeedItemUi): Boolean {
        if (oldItem::class.java != newItem::class.java || oldItem::class != newItem::class) return false
        return when (oldItem::class) {
            SeenKinoFeedItem::class -> areContentsTheSame(oldItem, newItem, FeedSeenDiffCallback)
            ActualKinoFeedItem::class -> areContentsTheSame(
                oldItem,
                newItem,
                FeedActualKinoDiffCallback
            )

            NewsKinoFeedItem::class -> areContentsTheSame(
                oldItem,
                newItem,
                FeedNewsKinoDiffCallback
            )

            else -> throw IllegalArgumentException("no supported Kotlin class")

        }
    }

    private inline fun <reified T : FeedItemUi> areContentsTheSame(
        a1: FeedItemUi,
        a2: FeedItemUi,
        b: DiffUtil.ItemCallback<T>
    ): Boolean {
        return b.areContentsTheSame(a1 as T, a2 as T)
    }

    private inline fun <reified T : FeedItemUi> areItemsTheSame(
        a1: FeedItemUi,
        a2: FeedItemUi,
        b: DiffUtil.ItemCallback<T>
    ): Boolean {
        return b.areItemsTheSame(a1 as T, a2 as T)
    }
}