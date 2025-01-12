package com.rrpvm.kinofeed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.domain.model.BaseKinoModel
import com.rrpvm.kinofeed.databinding.ItemKinoItemBinding
import com.rrpvm.kinofeed.presentation.viewholder.KinoViewHolder

const val ITEM_SIZE_MULTIPLIER = 0.3

class DefaultKinoListAdapter(private val onKinoSelected: ((kinoId: String) -> Unit)) :
    RecyclerView.Adapter<KinoViewHolder>() {
    private val mItems = mutableListOf<BaseKinoModel>()
    //callbacks

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
        ).apply {
            this.setOnKinoSelectedCallback(onKinoSelected)
        }
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

    fun setItems(list: List<BaseKinoModel>) {
        mItems.clear()
        mItems.addAll(list)
        notifyDataSetChanged()
    }
}