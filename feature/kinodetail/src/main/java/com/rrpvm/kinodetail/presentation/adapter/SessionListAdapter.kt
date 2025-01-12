package com.rrpvm.kinodetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.kinodetail.databinding.ItemSessionBinding
import com.rrpvm.kinodetail.presentation.model.SessionModelUi

class SessionListAdapter : RecyclerView.Adapter<SessionListAdapter.SessionViewHolder>() {
    private val items = mutableListOf<SessionModelUi>()


    class SessionViewHolder(private val binding: ItemSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: SessionModelUi) {
            binding.tvNumber.text = item.sessionIndexNormalized
            binding.tvSessionLabel.text =
                StringBuilder().append(item.sessionInfo).append(", ").append(item.label).toString()
        }
    }

    fun setItems(list: List<SessionModelUi>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()//все равно данные никак не меняются на экране деталей
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        return SessionViewHolder(
            ItemSessionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.onBind(items[position])
    }
}