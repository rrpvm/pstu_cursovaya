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
import com.rrpvm.domain.model.BaseKinoModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.kinofeed.databinding.ItemKinoItemBinding

class KinoViewHolder(private val binding: ItemKinoItemBinding) : ViewHolder(binding.root) {
    //fields
    private var viewTarget: ViewTarget<*, *>? = null
    private var boundKinoId: String? = null

    //callbacks
    private var onKinoSelected: ((kinoId: String) -> Unit)? = null


    init {
        binding.llKinoItemWorkspace.setOnClickListener {
            onKinoSelected?.invoke(boundKinoId ?: return@setOnClickListener)
        }
    }

    fun setOnKinoSelectedCallback(callback: (id: String) -> Unit) {
        onKinoSelected = callback
    }


    fun onBind(model: BaseKinoModel) {
        boundKinoId = model.id

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
        boundKinoId = null
        runCatching {
            Glide.with(binding.tvKinoTitle).clear(viewTarget)
        }
        binding.ivKinoPreview.setImageDrawable(null)
        binding.previewLoader.isVisible = true
    }
}