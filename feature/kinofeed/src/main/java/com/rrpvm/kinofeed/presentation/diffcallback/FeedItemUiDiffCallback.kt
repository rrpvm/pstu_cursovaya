package com.rrpvm.kinofeed.presentation.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.LikedKinoFeedItem
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

            LikedKinoFeedItem::class -> areItemsTheSame(
                oldItem,
                newItem,
                FeedLikedKinoDiffCallback
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

            LikedKinoFeedItem::class -> areContentsTheSame(
                oldItem,
                newItem,
                FeedLikedKinoDiffCallback
            )

            else -> throw IllegalArgumentException("no supported Kotlin class")

        }
    }

    override fun getChangePayload(oldItem: FeedItemUi, newItem: FeedItemUi): Any? {
        if (oldItem is ActualKinoFeedItem && newItem is ActualKinoFeedItem) {
            val payload = mutableListOf<ActualKinoFeedItem.Payloads>()
            if (oldItem.dateMode != newItem.dateMode) payload.add(ActualKinoFeedItem.Payloads.DateModeChanged)
            if (oldItem.kinoList != newItem.kinoList) payload.add(ActualKinoFeedItem.Payloads.KinoListChanged)
            return payload.takeIf { it.isNotEmpty() }
        }
        return super.getChangePayload(oldItem, newItem)
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