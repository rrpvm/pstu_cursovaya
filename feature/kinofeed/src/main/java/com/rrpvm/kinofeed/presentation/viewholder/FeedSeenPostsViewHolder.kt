package com.rrpvm.kinofeed.presentation.viewholder

import androidx.recyclerview.widget.PagerSnapHelper
import com.rrpvm.kinofeed.databinding.ItemFeedSeenPostsBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultKinoListAdapter
import com.rrpvm.kinofeed.presentation.decorator.KinoHorizontalDecorator
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.SeenKinoFeedItem

class FeedSeenPostsViewHolder(
    private val binding: ItemFeedSeenPostsBinding,
    onKinoItemSelected: (kinoId: String) -> Unit
) : KinoFeedDefaultViewHolder(binding.root) {
    private val mAdapter = DefaultKinoListAdapter(onKinoItemSelected)
    private val snapHelper = PagerSnapHelper()
    private val decorator = KinoHorizontalDecorator()

    init {
        snapHelper.attachToRecyclerView(binding.rvFeedSeenPosts)
        binding.rvFeedSeenPosts.addItemDecoration(decorator)
    }

    override fun onBind(item: FeedItemUi) {
        if (item !is SeenKinoFeedItem) return
        mAdapter.setItems(item.viewedKinoList)
        binding.rvFeedSeenPosts.adapter = mAdapter
    }

    override fun onViewRecycled() {
        binding.rvFeedSeenPosts.adapter = null

    }
}