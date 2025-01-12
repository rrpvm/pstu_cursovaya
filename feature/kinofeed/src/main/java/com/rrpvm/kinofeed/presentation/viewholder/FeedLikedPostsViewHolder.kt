package com.rrpvm.kinofeed.presentation.viewholder

import androidx.recyclerview.widget.PagerSnapHelper
import com.rrpvm.kinofeed.databinding.ItemFeedLikedBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultKinoListAdapter
import com.rrpvm.kinofeed.presentation.decorator.KinoHorizontalDecorator
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.LikedKinoFeedItem

class FeedLikedPostsViewHolder(
    private val binding: ItemFeedLikedBinding,
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
        if (item !is LikedKinoFeedItem) return
        mAdapter.setItems(item.likedKinoList)
        binding.rvFeedSeenPosts.adapter = mAdapter
    }

    override fun onViewRecycled() {
        binding.rvFeedSeenPosts.adapter = null

    }
}