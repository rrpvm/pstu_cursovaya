package com.rrpvm.profile.presentation.menu.viewholder

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.rrpvm.profile.databinding.ItemAccountInfoBinding
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem

class ClientInfoViewHolder(private val binding: ItemAccountInfoBinding) :
    ProfileMenuViewHolder(binding.root) {
    private var glideLoader: BaseTarget<*>? = null
    override fun onBindViewHolder(item: ProfileMenuItem) {
        if (item !is ProfileMenuItem.ClientItem) {
            throw IllegalArgumentException("incorrect viewtype")
        }
        binding.tvAccInfoDate.text = item.mCreationDate
        binding.tvAccInfoUser.text = "@${item.mClientName}"
        glideLoader = Glide.with(binding.ivAvatar.context.applicationContext)
            .load(item.avatarUri)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.cpiAvatar.hide()
                    binding.ivAvatar.setImageDrawable(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    binding.cpiAvatar.hide()
                    binding.ivAvatar.setImageResource(com.rrpvm.core.R.drawable.brush_8dp_solid)
                }
            })
    }

    override fun onViewRecycled() {
        runCatching {
            glideLoader?.let { loader ->
                Glide.with(binding.ivAvatar.context.applicationContext).clear(loader)
            }
        }
    }
}