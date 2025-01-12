package com.rrpvm.kinodetail.presentation.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.number.NumberFormatter
import android.icu.text.MeasureFormat
import android.icu.util.MeasureUnit
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.rrpvm.kinodetail.R
import com.rrpvm.kinodetail.databinding.FragmentKinoDetailBinding
import com.rrpvm.kinodetail.presentation.adapter.GenreListAdapter
import com.rrpvm.kinodetail.presentation.adapter.SessionListAdapter
import com.rrpvm.kinodetail.presentation.model.detail.KinoDetailViewData
import com.rrpvm.kinodetail.presentation.model.detail.SessionModelUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
        SessionListAdapter(viewModel::onBuyTicket)
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
                    viewModel.screenEffect.collectLatest {
                        when (it) {
                            is KinoDetailScreenEffect.GoBuyTicket -> {
                                findNavController().navigate(
                                    KinoDetailFragmentDirections.actionFragmentKinoDetailToFragmentKinoBuyTicket(
                                        it.sessionId
                                    )
                                )
                            }
                        }
                    }
                }
                launch {
                    viewModel.sessionsAdapterData.collectLatest {
                        sessionAdapter.setItems(it.mapIndexed { index, baseShortSessionModel ->
                            SessionModelUi(
                                sessionId = baseShortSessionModel.sessionId,
                                sessionIndexNormalized = index.inc().toString(),
                                label = sessionShowDateFormat.format(baseShortSessionModel.sessionDate),
                                sessionInfo = baseShortSessionModel.sessionInfo
                            )
                        })
                    }
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivKinoLike.setOnClickListener {
            viewModel.onLike()
        }
        binding.rvSessionList.adapter = sessionAdapter
        binding.rvGenres.adapter = genreAdapter
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
        with(uiState.kino.base) {
            binding.ivKinoTitle.text = kinoModel.title
            Glide.with(binding.ivKinoLike)
                .load(
                    if (uiState.kino.base.kinoModel.isLiked) {
                        com.rrpvm.core.R.drawable.ic_like
                    } else {
                        com.rrpvm.core.R.drawable.ic_like_outline
                    }
                )
                .into(binding.ivKinoLike)
            val filmDuration = (kinoModel.duration / 60).toDuration(DurationUnit.MINUTES)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                binding.tvDuration.text =
                    resources.getString(
                        R.string.duration_format,
                        NumberFormatter.withLocale(Locale.getDefault()).unit(MeasureUnit.MINUTE)
                            .sign(NumberFormatter.SignDisplay.NEVER)
                            .unitWidth(NumberFormatter.UnitWidth.SHORT)
                            .format(filmDuration.inWholeMinutes).toString()
                    )
            } else {
                binding.tvDuration.text =
                    resources.getString(
                        R.string.duration_format,
                        filmDuration.toString() + MeasureFormat.getInstance(
                            Locale.getDefault(),
                            MeasureFormat.FormatWidth.SHORT
                        ).getUnitDisplayName(MeasureUnit.MINUTE)
                    )
            }

            binding.tvReleasedDate.text =
                resources.getString(
                    R.string.released_format,
                    releaseDateFormat.format(kinoModel.releasedDate)
                )

            binding.tvDescription.text = kinoModel.description
            binding.llAgeRating.isVisible = uiState.kino.ageRatingModel != null
            uiState.kino.ageRatingModel?.let {
                Glide.with(binding.ivAgeRating).load(
                    it.urlIcon
                ).error(com.rrpvm.core.R.drawable.ic_image_error)
                    .into(binding.ivAgeRating)
            }


            loadingTarget = Glide.with(binding.ivKinoFull)
                .asBitmap()
                .load(kinoModel.previewImage)
                .error(R.drawable.error_loading_image_with_surface)
                .placeholder(com.rrpvm.core.R.color.surface_contrast)
                .transform(RoundedCorners((resources.displayMetrics.density * 8).toInt()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onLoadStarted(placeholder: Drawable?) {
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

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        _binding?.imageProgress?.isVisible = false
                        _binding?.ivKinoFull?.setImageBitmap(resource)
                    }
                })
        }

    }

    override fun onDestroyView() {
        binding.rvSessionList.adapter = null
        binding.rvGenres.adapter = null
        _binding = null
        kotlin.runCatching {
            loadingTarget?.let {
                Glide.with(this).clear(it)
            }
        }
        super.onDestroyView()
    }

    companion object {
        private val releaseDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        private val sessionShowDateFormat = SimpleDateFormat("EE, dd MMMM с HH:mm", Locale("Ru"))
    }
}