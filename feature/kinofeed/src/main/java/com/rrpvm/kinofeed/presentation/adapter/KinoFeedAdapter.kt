package com.rrpvm.kinofeed.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.kinofeed.presentation.diffcallback.FeedItemUiDiffCallback
import com.rrpvm.kinofeed.presentation.model.FeedItemUiTypes
import com.rrpvm.kinofeed.presentation.viewholder.KinoFeedDefaultViewHolder

class KinoFeedAdapter : RecyclerView.Adapter<KinoFeedDefaultViewHolder>() {
    private val mItems = AsyncListDiffer(this, FeedItemUiDiffCallback())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinoFeedDefaultViewHolder {
        return when (FeedItemUiTypes.entries.first { it.viewType == viewType }) {
            FeedItemUiTypes.SEEN_POSTS -> KinoFeedDefaultViewHolder(View(parent.context))
            FeedItemUiTypes.ALL_POSTS -> KinoFeedDefaultViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: KinoFeedDefaultViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return mItems.currentList.size
    }
}