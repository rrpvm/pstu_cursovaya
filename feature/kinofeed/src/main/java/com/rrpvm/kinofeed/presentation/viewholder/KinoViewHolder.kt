package com.rrpvm.kinofeed.presentation.viewholder

import android.graphics.drawable.Drawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.kinofeed.databinding.ItemKinoItemBinding

class KinoViewHolder(private val binding: ItemKinoItemBinding) : ViewHolder(binding.root) {
    private var viewTarget: ViewTarget<*, *>? = null
    fun onBind(model: KinoModel) {
        binding.previewLoader.isVisible = true
        binding.tvKinoTitle.text = model.title
        viewTarget = Glide.with(binding.ivKinoPreview)
            .load(model.previewImage)
            .centerCrop()
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.previewLoader.isVisible = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.previewLoader.isVisible = false
                    return false
                }
            })
            .error(com.rrpvm.core.R.drawable.ic_image_error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivKinoPreview)
            .clearOnDetach()
    }

    fun onViewRecycled() {
        runCatching {
            Glide.with(binding.tvKinoTitle).clear(viewTarget)
        }
        binding.ivKinoPreview.setImageDrawable(null)
        binding.previewLoader.isVisible = true
    }
}