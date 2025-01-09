package com.rrpvm.kinofeed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.kinofeed.databinding.ItemKinoItemBinding
import com.rrpvm.kinofeed.presentation.viewholder.KinoViewHolder

const val ITEM_SIZE_MULTIPLIER = 0.3

class DefaultKinoListAdapter : RecyclerView.Adapter<KinoViewHolder>() {
    private val mItems = mutableListOf<KinoModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinoViewHolder {
        return KinoViewHolder(
            ItemKinoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                this.root.layoutParams = RecyclerView.LayoutParams(
                    (parent.measuredWidth * ITEM_SIZE_MULTIPLIER).toInt(),
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }

    override fun onViewRecycled(holder: KinoViewHolder) {
        holder.onViewRecycled()
    }

    override fun onBindViewHolder(holder: KinoViewHolder, position: Int) {
        holder.onBind(mItems[position])
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun setItems(list: List<KinoModel>) {
        mItems.clear()
        mItems.addAll(list)
        notifyDataSetChanged()
    }
}