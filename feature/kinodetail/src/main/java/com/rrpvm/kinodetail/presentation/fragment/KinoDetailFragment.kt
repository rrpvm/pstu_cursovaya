package com.rrpvm.kinodetail.presentation.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.rrpvm.kinodetail.R
import com.rrpvm.kinodetail.databinding.FragmentKinoDetailBinding
import com.rrpvm.kinodetail.presentation.adapter.GenreListAdapter
import com.rrpvm.kinodetail.presentation.adapter.SessionListAdapter
import com.rrpvm.kinodetail.presentation.model.KinoDetailViewData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KinoDetailFragment : Fragment() {
    private val viewModel: KinoDetailViewModel by viewModels()
    private var _binding: FragmentKinoDetailBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val safeArgs by navArgs<KinoDetailFragmentArgs>()
    private val genreAdapter by lazy {
        GenreListAdapter()
    }
    private val sessionAdapter by lazy {
        SessionListAdapter()
    }
    private var loadingTarget: Target<*>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentKinoDetailBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.screenState.collectLatest {
                        onUiUpdate(it)
                    }
                }
                launch {
                    viewModel.genresAdapterData.collectLatest {
                        genreAdapter.setItems(it)
                    }
                }
                launch {
                    viewModel.sessionsAdapterData.collectLatest {
                        sessionAdapter.setItems(it)
                    }
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.rvGenres.adapter = genreAdapter
        binding.rvSessionList.adapter = sessionAdapter
    }

    private fun onUiUpdate(uiState: KinoDetailViewData) {
        when (uiState) {
            KinoDetailViewData.ScreenLoading -> {
                binding.lavLoader.isVisible = true
                binding.contentRoot.isVisible = false
            }

            KinoDetailViewData.ScreenFail -> {
                binding.lavLoader.isVisible = false
                binding.contentRoot.isVisible = false
            }

            is KinoDetailViewData.Success -> {
                binding.contentRoot.isVisible = true
                binding.lavLoader.isVisible = false
                onUiSuccess(uiState)
            }
        }
    }

    private fun onUiSuccess(uiState: KinoDetailViewData.Success) {

        binding.ivKinoTitle.text = uiState.kino.kinoModel.title
        binding.tvDuration.text = "Duration: 2h15m"
        binding.tvReleasedDate.text = uiState.kino.kinoModel.releasedDate.toString()
        binding.tvDescription.text = uiState.kino.kinoModel.description
        loadingTarget = Glide.with(binding.ivKinoFull)
            .asBitmap()
            .load(uiState.kino.kinoModel.previewImage)
            .error(R.drawable.error_loading_image_with_surface)
            .placeholder(com.rrpvm.core.R.color.surface_contrast)

            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>,
                    isFirstResource: Boolean
                ): Boolean {
                    _binding?.ivKinoFull?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        this.dimensionRatio = "16:9"
                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    model: Any,
                    target: Target<Bitmap>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {

                    val width = resource.width.toFloat()
                    val height = resource.height.toFloat()
                    val ratio = width / height
                    _binding?.ivKinoFull?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        this.dimensionRatio = "$ratio"
                    }
                    return false
                }
            })
            .transform(RoundedCorners((resources.displayMetrics.density * 12).toInt()))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onLoadStarted(placeholder: Drawable?) {
                    _binding?.ivKinoFull?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                        this.dimensionRatio = "16:9"
                    }
                    _binding?.ivKinoFull?.setImageDrawable(placeholder)
                    _binding?.imageProgress?.isVisible = true
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    _binding?.ivKinoFull?.setImageDrawable(placeholder)
                    _binding?.imageProgress?.isVisible = true
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    _binding?.ivKinoFull?.setImageDrawable(errorDrawable)
                    _binding?.imageProgress?.isVisible = false
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    _binding?.imageProgress?.isVisible = false
                    _binding?.ivKinoFull?.setImageBitmap(resource)
                }
            })
    }

    override fun onDestroyView() {
        binding.rvGenres.adapter = null
        binding.rvSessionList.adapter = null
        _binding = null
        kotlin.runCatching {
            loadingTarget?.let {
                Glide.with(this).clear(it)
            }
        }
        super.onDestroyView()
    }
}