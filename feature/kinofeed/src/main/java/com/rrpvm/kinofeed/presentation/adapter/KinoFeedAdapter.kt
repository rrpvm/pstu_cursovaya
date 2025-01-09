package com.rrpvm.kinofeed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.kinofeed.databinding.ItemFeedActualDayPostsBinding
import com.rrpvm.kinofeed.databinding.ItemFeedSeenPostsBinding
import com.rrpvm.kinofeed.presentation.diffcallback.FeedItemUiDiffCallback
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.FeedItemUiTypes
import com.rrpvm.kinofeed.presentation.viewholder.FeedActualDayPostsViewHolder
import com.rrpvm.kinofeed.presentation.viewholder.FeedSeenPostsViewHolder
import com.rrpvm.kinofeed.presentation.viewholder.KinoFeedDefaultViewHolder

class KinoFeedAdapter(
    private val actualFeedItemListener: ActualFeedItemListener,
) : RecyclerView.Adapter<KinoFeedDefaultViewHolder>() {
    private val mItems = AsyncListDiffer(this, FeedItemUiDiffCallback())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinoFeedDefaultViewHolder {
        return when (FeedItemUiTypes.entries.first { it.viewType == viewType }) {
            FeedItemUiTypes.SEEN_POSTS -> FeedSeenPostsViewHolder(
                ItemFeedSeenPostsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            FeedItemUiTypes.ACTUAL_POSTS -> FeedActualDayPostsViewHolder(
                ItemFeedActualDayPostsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).also {
                it.setOnShiftLeftDateDateModeListener(actualFeedItemListener::onShiftLeft)
                it.setOnShiftRightDateDateModeListener(actualFeedItemListener::onShiftRight)
            }

            // FeedItemUiTypes.NEW_POSTS -> KinoFeedDefaultViewHolder(View(parent.context))
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: KinoFeedDefaultViewHolder, position: Int) {
        holder.onBind(mItems.currentList[position])
    }

    override fun onViewRecycled(holder: KinoFeedDefaultViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun getItemViewType(position: Int): Int {
        return mItems.currentList[position].mViewType
    }

    override fun getItemCount(): Int {
        return mItems.currentList.size
    }

    fun setItems(list: List<FeedItemUi>) {
        mItems.submitList(list)
    }
}