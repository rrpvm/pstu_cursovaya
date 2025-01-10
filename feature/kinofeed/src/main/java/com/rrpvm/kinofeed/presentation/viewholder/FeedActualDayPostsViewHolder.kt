package com.rrpvm.kinofeed.presentation.viewholder

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.recyclerview.widget.PagerSnapHelper
import com.rrpvm.kinofeed.databinding.ItemFeedActualDayPostsBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultKinoListAdapter
import com.rrpvm.kinofeed.presentation.decorator.KinoHorizontalDecorator
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.PickDateModeUi

class FeedActualDayPostsViewHolder(
    private val binding: ItemFeedActualDayPostsBinding,
    onKinoItemSelected: (kinoId: String) -> Unit
) :
    KinoFeedDefaultViewHolder(binding.root) {
    private val mAdapter = DefaultKinoListAdapter(onKinoItemSelected)
    private val snapHelper = PagerSnapHelper()
    private val decorator = KinoHorizontalDecorator()




    //callbacks
    private var onShiftRightDateModeListener: (() -> Unit)? = null
    private var onShiftLeftDateModeListener: (() -> Unit)? = null


    fun setOnShiftRightDateDateModeListener(callback: () -> Unit) {
        onShiftRightDateModeListener = callback
    }

    fun setOnShiftLeftDateDateModeListener(callback: () -> Unit) {
        onShiftLeftDateModeListener = callback
    }

    init {
        snapHelper.attachToRecyclerView(binding.rvFeedActualDayPosts)
        binding.rvFeedActualDayPosts.addItemDecoration(decorator)
        binding.btnDateModeShiftLeft.setOnClickListener {
            onShiftLeftDateModeListener?.invoke()
        }
        binding.btnDateModeShiftRight.setOnClickListener {
            onShiftRightDateModeListener?.invoke()
        }
    }

    override fun onBind(item: FeedItemUi) {
        if (item !is ActualKinoFeedItem) return

        binding.rvFeedActualDayPosts.adapter = mAdapter
        binding.tvDateMode.setText(item.dateMode.resId)
        if (item.dateMode.ordinal == 0) {
            binding.btnDateModeShiftLeft.setDisabled()

        } else {
            binding.btnDateModeShiftLeft.setEnabled()
        }
        if (item.dateMode.ordinal == PickDateModeUi.entries.size - 1) {
            binding.btnDateModeShiftRight.setDisabled()
        } else {
            binding.btnDateModeShiftRight.setEnabled()
        }
        mAdapter.setItems(item.kinoList)
    }

    private fun ImageView.setDisabled() {
        imageTintList = ColorStateList.valueOf(
            itemView.resources.getColor(
                com.rrpvm.core.R.color.surface,
                itemView.context.theme
            )
        )
    }

    private fun ImageView.setEnabled() {
        imageTintList = ColorStateList.valueOf(
            itemView.resources.getColor(
                com.rrpvm.core.R.color.p40,
                itemView.context.theme
            )
        )
    }

    override fun onViewRecycled() {
        binding.rvFeedActualDayPosts.adapter = null
        binding.tvDateMode.text = null
    }
}