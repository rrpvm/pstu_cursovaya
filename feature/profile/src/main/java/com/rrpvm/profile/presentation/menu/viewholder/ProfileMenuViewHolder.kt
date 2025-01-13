package com.rrpvm.profile.presentation.menu.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rrpvm.profile.presentation.model.ProfileMenuItem


open class ProfileMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun  onBindViewHolder(item: ProfileMenuItem) {
        throw RuntimeException("no specified")
    }
    open fun onViewRecycled(){

    }
}