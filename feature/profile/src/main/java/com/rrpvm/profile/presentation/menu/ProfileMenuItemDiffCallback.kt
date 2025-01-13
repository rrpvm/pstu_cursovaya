package com.rrpvm.profile.presentation.menu

import androidx.recyclerview.widget.DiffUtil
import com.rrpvm.profile.presentation.model.ProfileMenuItem

class ProfileMenuItemDiffCallback : DiffUtil.ItemCallback<ProfileMenuItem>() {
    override fun areItemsTheSame(oldItem: ProfileMenuItem, newItem: ProfileMenuItem): Boolean {
      return oldItem.viewType == newItem.viewType && oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProfileMenuItem, newItem: ProfileMenuItem): Boolean {
        return oldItem == newItem
    }
}