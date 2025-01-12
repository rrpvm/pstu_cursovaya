package com.rrpvm.kinofeed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.kinofeed.databinding.ItemFeedActualDayPostsBinding
import com.rrpvm.kinofeed.databinding.ItemFeedLikedBinding
import com.rrpvm.kinofeed.databinding.ItemFeedSeenPostsBinding
import com.rrpvm.kinofeed.presentation.diffcallback.FeedItemUiDiffCallback
import com.rrpvm.kinofeed.presentation.listener.ActualFeedItemListener
import com.rrpvm.kinofeed.presentation.listener.SeenFeedItemListener
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.FeedItemUiTypes
import com.rrpvm.kinofeed.presentation.viewholder.FeedActualDayPostsViewHolder
import com.rrpvm.kinofeed.presentation.viewholder.FeedLikedPostsViewHolder
import com.rrpvm.kinofeed.presentation.viewholder.FeedSeenPostsViewHolder
import com.rrpvm.kinofeed.presentation.viewholder.KinoFeedDefaultViewHolder
import java.lang.ref.WeakReference

class KinoFeedAdapter(
    private val actualFeedItemListener: ActualFeedItemListener,
    private val seenFeedItemListener: SeenFeedItemListener,
) : RecyclerView.Adapter<KinoFeedDefaultViewHolder>() {
    private val mItems = AsyncListDiffer(this, FeedItemUiDiffCallback())
    private var recycler = WeakReference<RecyclerView>(null)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recycler = WeakReference(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinoFeedDefaultViewHolder {
        return when (FeedItemUiTypes.entries.first { it.viewType == viewType }) {
            FeedItemUiTypes.SEEN_POSTS -> FeedSeenPostsViewHolder(
                binding = ItemFeedSeenPostsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onKinoItemSelected = seenFeedItemListener::onItemSelected
            )

            FeedItemUiTypes.ACTUAL_POSTS -> FeedActualDayPostsViewHolder(
                binding = ItemFeedActualDayPostsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onKinoItemSelected = actualFeedItemListener::onKinoSelected,
                onRefreshFetchData = actualFeedItemListener::onRetryFetchData
            ).also {
                it.setOnShiftLeftDateDateModeListener(actualFeedItemListener::onShiftLeft)
                it.setOnShiftRightDateDateModeListener(actualFeedItemListener::onShiftRight)
            }

            FeedItemUiTypes.LIKED_POSTS -> FeedLikedPostsViewHolder(
                binding = ItemFeedLikedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onKinoItemSelected = seenFeedItemListener::onItemSelected
            )
        }
    }

    override fun onBindViewHolder(holder: KinoFeedDefaultViewHolder, position: Int) {
        holder.onBind(mItems.currentList[position])
    }

    override fun onBindViewHolder(
        holder: KinoFeedDefaultViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        if (holder is FeedActualDayPostsViewHolder) {
            holder.onBindByChanges(mItems.currentList[position], payloads)
            return
        } else super.onBindViewHolder(holder, position, payloads)
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

    private var wasKinoActual: Boolean = false
    fun setItems(list: List<FeedItemUi>) {
        mItems.submitList(list) {
            val newKinoActual = list.any { it is ActualKinoFeedItem }
            if (newKinoActual != wasKinoActual && newKinoActual) {
                this.recycler.get()?.scrollToPosition(0)
            }
        }
    }
}