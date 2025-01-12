package com.rrpvm.kinodetail.presentation.adapter


import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.domain.model.HallPlaceModel
import com.rrpvm.kinodetail.databinding.ItemPlaceBinding
import com.rrpvm.kinodetail.presentation.viewholder.HallPlaceViewHolder

class HallPlaceAdapter(
    private val onPickItemCallback: (Int) -> Unit,
) : RecyclerView.Adapter<HallPlaceViewHolder>() {
    private val list = mutableListOf<HallPlaceModel>()

    private var greenColorState: ColorStateList? = null
    private var redColorState: ColorStateList? = null
    private var unusedColorState: ColorStateList? = null
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        greenColorState = ColorStateList.valueOf(
            recyclerView.resources.getColor(
                com.rrpvm.core.R.color.client_selected,
                recyclerView.context.theme
            )
        )
        redColorState = ColorStateList.valueOf(
            recyclerView.resources.getColor(
                com.rrpvm.core.R.color.e40,
                recyclerView.context.theme
            )
        )
        unusedColorState = ColorStateList.valueOf(
            recyclerView.resources.getColor(
                com.rrpvm.core.R.color.unused_place,
                recyclerView.context.theme
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HallPlaceViewHolder {
        return HallPlaceViewHolder(
            ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onPickItemCallback
        )
    }

    override fun onBindViewHolder(holder: HallPlaceViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvIndex.text = item.index.inc().toString()
        holder.binding.root.backgroundTintList = if (item.isBusyByOther) {
            redColorState
        } else if (item.isBusyByUser) {
            greenColorState
        } else {
            unusedColorState
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItems(newList: List<HallPlaceModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()//я панк, я так чувствую
    }
}