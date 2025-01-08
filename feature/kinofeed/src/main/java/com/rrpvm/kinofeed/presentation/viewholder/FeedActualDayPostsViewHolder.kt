package com.rrpvm.kinofeed.presentation.viewholder

import androidx.recyclerview.widget.PagerSnapHelper
import com.rrpvm.kinofeed.databinding.ItemFeedActualDayPostsBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultKinoListAdapter
import com.rrpvm.kinofeed.presentation.decorator.KinoHorizontalDecorator
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi

class FeedActualDayPostsViewHolder(private val binding: ItemFeedActualDayPostsBinding) :
    KinoFeedDefaultViewHolder(binding.root) {
    private val mAdapter = DefaultKinoListAdapter()
    private val snapHelper = PagerSnapHelper()
    private val decorator = KinoHorizontalDecorator()

    init {
        snapHelper.attachToRecyclerView(binding.rvFeedActualDayPosts)
        binding.rvFeedActualDayPosts.addItemDecoration(decorator)
    }

    override fun onBind(item: FeedItemUi) {
        if (item !is ActualKinoFeedItem) return
        binding.rvFeedActualDayPosts.adapter = mAdapter
        mAdapter.setItems(item.kinoList)

    }

    override fun onViewRecycled() {
        binding.rvFeedActualDayPosts.adapter = null
    }
}