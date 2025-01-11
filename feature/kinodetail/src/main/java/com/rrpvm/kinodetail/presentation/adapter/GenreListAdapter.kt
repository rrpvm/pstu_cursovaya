package com.rrpvm.kinodetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.kinodetail.databinding.ItemGenreBinding


class GenreListAdapter : RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>() {
    private val items = mutableListOf<GenreModel>()


    class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: GenreModel) {
            binding.tvGenreName.text = item.title
        }
    }

    fun setItems(list: List<GenreModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()//все равно данные никак не меняются на экране деталей
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            ItemGenreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.onBind(items[position])
    }
}