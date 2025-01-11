package com.rrpvm.kinodetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.domain.model.BaseShortSessionModel
import com.rrpvm.kinodetail.databinding.ItemSessionBinding

class SessionListAdapter : RecyclerView.Adapter<SessionListAdapter.SessionViewHolder>() {
    private val items = mutableListOf<BaseShortSessionModel>()


    class SessionViewHolder(private val binding: ItemSessionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: BaseShortSessionModel, position: Int) {
            binding.tvNumber.text = (position+1).toString()
            binding.tvSessionLabel.text = item.sessionDate.toString()
        }
    }

    fun setItems(list: List<BaseShortSessionModel>) {
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
        holder.onBind(items[position], position)
    }
}