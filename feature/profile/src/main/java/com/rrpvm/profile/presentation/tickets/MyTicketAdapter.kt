package com.rrpvm.profile.presentation.tickets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.rrpvm.profile.databinding.ItemTicketBinding

class MyTicketAdapter : RecyclerView.Adapter<MyTicketAdapter.ViewHolder>() {
    private val mItems = mutableListOf<TicketUi>()

    class ViewHolder(private val binding: ItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(ticket: TicketUi) {
            binding.tvTitle.text = "Фильм: ${ticket.kinoTitle}"
            binding.tvDate.text = "Дата начала: ${ticket.sessionStart}"
            binding.tvPlace.text = "Место: ${ticket.placeRow} ряд ${ticket.placeColumn} колонка ${ticket.place} место"
            binding.tvPlaceV2.text = "Кинозал: ${ticket.hallName}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mItems[position])
    }

    fun setItems(items: List<TicketUi>) {
        mItems.clear()
        mItems.addAll(items)
        notifyDataSetChanged()
    }
}