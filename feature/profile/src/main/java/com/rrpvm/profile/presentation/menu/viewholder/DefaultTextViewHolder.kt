package com.rrpvm.profile.presentation.menu.viewholder

import com.rrpvm.profile.databinding.ItemDefaultTextBinding
import com.rrpvm.profile.presentation.menu.model.DefaultTextMenuTypes
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem

class DefaultTextViewHolder(
    private val binding: ItemDefaultTextBinding,
    onClickListener: (calledBy: DefaultTextMenuTypes) -> Unit
) : ProfileMenuViewHolder(binding.root) {
    private var boundType: DefaultTextMenuTypes? = null

    init {
        binding.llItemDefaultTextRoot.setOnClickListener {
            onClickListener(boundType ?: return@setOnClickListener)
        }
    }

    override fun onBindViewHolder(item: ProfileMenuItem) {
        if (item !is ProfileMenuItem.DefaultTextMenu) {
            throw IllegalArgumentException("incorrect logic")
        }
        boundType = item.type
        binding.tvProfileMenuItem.text = item.mText
        binding.ivProfileMenuItem.setImageResource(item.mIcon)
    }

    override fun onViewRecycled() {
        boundType = null
    }
}