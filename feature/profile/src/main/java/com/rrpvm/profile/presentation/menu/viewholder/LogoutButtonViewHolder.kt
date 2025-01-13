package com.rrpvm.profile.presentation.menu.viewholder

import com.rrpvm.profile.databinding.ItemLogoutButtonBinding
import com.rrpvm.profile.presentation.model.ProfileMenuItem

class LogoutButtonViewHolder(
    private val binding: ItemLogoutButtonBinding,
    onLogoutListener: () -> Unit
) : ProfileMenuViewHolder(binding.root) {
    init {
        binding.btnLogout.setOnClickListener {
            onLogoutListener()
        }
    }

    override fun onBindViewHolder(item: ProfileMenuItem) {

    }
}