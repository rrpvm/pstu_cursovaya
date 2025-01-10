package com.rrpvm.kinofeed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.rrpvm.kinofeed.databinding.ItemCheckboxFilterBinding
import com.rrpvm.kinofeed.domain.FilterItem

class DefaultFilterAdapter(private val onStateChanged: (String, Boolean) -> Unit) :
    RecyclerView.Adapter<DefaultFilterAdapter.FilterViewHolder>() {
    private val list = mutableListOf<FilterItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemCheckboxFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onStateChanged
        )
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun onViewRecycled(holder: FilterViewHolder) {
        holder.onViewRecycled()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(newList: List<FilterItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class FilterViewHolder(
        private val binding: ItemCheckboxFilterBinding,
        onStateChanged: (String, Boolean) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var label: String? = null

        init {
            binding.mcbFilter.addOnCheckedStateChangedListener { checkBox, state ->
                onStateChanged(
                    label ?: return@addOnCheckedStateChangedListener,
                    state == MaterialCheckBox.STATE_CHECKED
                )
            }
        }

        fun onBind(item: FilterItem) {
            label = item.value
            binding.tvFilterLabel.text = item.value
            binding.mcbFilter.isSelected = item.isEnabled
        }

        fun onViewRecycled() {
            label = null
        }
    }
}