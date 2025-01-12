package com.rrpvm.kinodetail.presentation.viewholder


import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.kinodetail.databinding.ItemPlaceBinding

class HallPlaceViewHolder(
    val binding: ItemPlaceBinding,
    private val onItemPick: (index: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onItemPick.invoke(this.adapterPosition.takeIf { it != -1 } ?: return@setOnClickListener)
        }
    }

}