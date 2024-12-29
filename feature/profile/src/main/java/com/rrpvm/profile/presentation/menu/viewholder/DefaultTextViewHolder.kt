package com.rrpvm.profile.presentation.menu.viewholder

import com.rrpvm.profile.databinding.ItemDefaultTextBinding
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem

class DefaultTextViewHolder(private val binding: ItemDefaultTextBinding) :
    ProfileMenuViewHolder(binding.root) {
    init {
            
    }

    override fun onBindViewHolder(item: ProfileMenuItem) {
        if (item !is ProfileMenuItem.DefaultTextMenu) {
            throw IllegalArgumentException("incorrect logic")
        }
        binding.tvProfileMenuItem.text = item.mText
        binding.ivProfileMenuItem.setImageResource(item.mIcon)
    }
}