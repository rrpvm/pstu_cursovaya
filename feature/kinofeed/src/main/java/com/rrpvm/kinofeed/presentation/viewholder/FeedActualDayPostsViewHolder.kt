package com.rrpvm.kinofeed.presentation.viewholder

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.PagerSnapHelper
import com.rrpvm.kinofeed.databinding.ItemFeedActualDayPostsBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultKinoListAdapter
import com.rrpvm.kinofeed.presentation.decorator.KinoHorizontalDecorator
import com.rrpvm.kinofeed.presentation.model.ActualKinoFeedItem
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.PickDateModeUi

class FeedActualDayPostsViewHolder(
    private val binding: ItemFeedActualDayPostsBinding,
    onKinoItemSelected: (kinoId: String) -> Unit,
    onRefreshFetchData: () -> Unit,
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
        binding.layoutNoContent.btnRefreshData.setOnClickListener {
            onRefreshFetchData()
        }
    }

    override fun onBind(item: FeedItemUi) {
        if (item !is ActualKinoFeedItem) return
        bindDatePicker(item)
        bindList(item)
        binding.rvFeedActualDayPosts.adapter = mAdapter
    }

    private fun bindDatePicker(item: ActualKinoFeedItem) {
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
    }

    private fun bindList(item: ActualKinoFeedItem) {
        binding.layoutNoContent.root.isVisible = item.kinoList.isEmpty()
        binding.rvFeedActualDayPosts.isVisible = item.kinoList.isNotEmpty()
        mAdapter.setItems(item.kinoList)
    }

    fun onBindByChanges(item: FeedItemUi, payload: List<*>) {
        if (item !is ActualKinoFeedItem) return
        for (_payload in payload) {
            if (_payload !is List<*>) continue
            _payload.filterIsInstance<ActualKinoFeedItem.Payloads>().forEach {
                when (it) {
                    ActualKinoFeedItem.Payloads.DateModeChanged -> {
                        bindDatePicker(item)
                    }

                    ActualKinoFeedItem.Payloads.KinoListChanged -> {
                        bindList(item)
                    }
                }
            }
        }
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