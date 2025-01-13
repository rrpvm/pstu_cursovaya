package com.rrpvm.profile.presentation.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.profile.databinding.ItemAccountInfoBinding
import com.rrpvm.profile.databinding.ItemDefaultTextBinding
import com.rrpvm.profile.databinding.ItemLogoutButtonBinding
import com.rrpvm.profile.presentation.model.DefaultTextMenuTypes
import com.rrpvm.profile.presentation.model.ProfileMenuItem
import com.rrpvm.profile.presentation.menu.viewholder.ClientInfoViewHolder
import com.rrpvm.profile.presentation.menu.viewholder.DefaultTextViewHolder
import com.rrpvm.profile.presentation.menu.viewholder.LogoutButtonViewHolder
import com.rrpvm.profile.presentation.menu.viewholder.ProfileMenuViewHolder

class ProfileMenuAdapter(
    private val onLogoutClicked: () -> Unit,
    private val onMenuOptionClicked: (type: DefaultTextMenuTypes) -> Unit,
) : RecyclerView.Adapter<ProfileMenuViewHolder>() {
    private val mItems = AsyncListDiffer<ProfileMenuItem>(this, ProfileMenuItemDiffCallback())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMenuViewHolder {
        return when (viewType) {
            ProfileMenuItem.DefaultTextMenu.VIEW_TYPE -> {
                DefaultTextViewHolder(
                    binding = ItemDefaultTextBinding.inflate(
                        /* inflater = */ LayoutInflater.from(parent.context),
                        /* parent = */ parent,
                        /* attachToParent = */ false
                    ),
                    onClickListener = onMenuOptionClicked
                )
            }

            ProfileMenuItem.ClientItem.VIEW_TYPE -> {
                ClientInfoViewHolder(
                    ItemAccountInfoBinding.inflate(
                        /* inflater = */ LayoutInflater.from(parent.context),
                        /* parent = */ parent,
                        /* attachToParent = */ false
                    )
                )
            }

            ProfileMenuItem.LogoutButtonItem.VIEW_TYPE -> {
                LogoutButtonViewHolder(
                    binding = ItemLogoutButtonBinding.inflate(
                        /* inflater = */ LayoutInflater.from(parent.context),
                        /* parent = */ parent,
                        /* attachToParent = */ false
                    ),
                    onLogoutListener = onLogoutClicked
                )
            }

            else -> {
                throw IllegalArgumentException("unknown view type")
            }
        }
    }

    override fun onBindViewHolder(holder: ProfileMenuViewHolder, position: Int) {
        holder.onBindViewHolder(mItems.currentList[position])
    }

    override fun getItemCount(): Int {
        return mItems.currentList.size
    }

    override fun onViewRecycled(holder: ProfileMenuViewHolder) {
        holder.onViewRecycled()
    }

    override fun getItemViewType(position: Int): Int {
        return mItems.currentList[position].viewType
    }

    fun setNewData(data: List<ProfileMenuItem>) {
        this.mItems.submitList(data)
    }

}