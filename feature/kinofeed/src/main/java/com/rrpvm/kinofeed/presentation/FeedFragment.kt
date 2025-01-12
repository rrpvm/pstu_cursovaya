package com.rrpvm.kinofeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rrpvm.kinofeed.databinding.FragmentKinoFeedBinding
import com.rrpvm.kinofeed.presentation.listener.ActualFeedItemListener
import com.rrpvm.kinofeed.presentation.adapter.KinoFeedAdapter
import com.rrpvm.kinofeed.presentation.listener.SeenFeedItemListener
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import com.rrpvm.kinofeed.presentation.model.MainFeedViewEffect
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private var _binding: FragmentKinoFeedBinding? = null
    private val binding get() = checkNotNull(_binding)
    private var mAdapter: KinoFeedAdapter? = null
    private val viewModel: FeedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onRetryFetchData()
        mAdapter = KinoFeedAdapter(
            actualFeedItemListener = viewModel as ActualFeedItemListener,
            seenFeedItemListener = viewModel as SeenFeedItemListener
        )
    }

    override fun onDestroy() {
        mAdapter = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentKinoFeedBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvKinoFeed.adapter = mAdapter
        binding.setupClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.networkFetchState.collectLatest { visibility ->
                        binding.updateIndicator.isVisible = visibility
                    }
                }
                launch {
                    viewModel.viewScreenEffects.collect { effect ->
                        resolveScreenEffect(effect)
                    }
                }
                launch {
                    viewModel.mAdapterLoadingState.collectLatest { isLoading ->
                        binding.llMainProgressContainer.isVisible = isLoading//шиммер
                        binding.ccContent.isVisible = isLoading.not()//шапка
                        if (isLoading) {
                            binding.sflShimmer.showShimmer(true)
                        } else {
                            binding.sflShimmer.hideShimmer()
                        }
                    }
                }
                launch {
                    viewModel.mAdapterState.collectLatest { adapterData: List<FeedItemUi>? ->
                        mAdapter?.setItems(adapterData ?: emptyList())
                    }
                }
            }
        }
    }

    private fun FragmentKinoFeedBinding.setupClickListeners() {
        this.settingbar.tvGenres.setOnClickListener {
            viewModel.onGenreFilterClicked()
        }
        this.settingbar.tvYear.setOnClickListener {
            viewModel.onYearFilterClicked()
        }
        this.settingbar.tvCountry.setOnClickListener {
            viewModel.onCountryFilterClicked()
        }
    }

    private fun resolveScreenEffect(effect: MainFeedViewEffect) {
        when (effect) {
            MainFeedViewEffect.OpenYearFilter -> {
                val action = FeedFragmentDirections.actionFragmentFeedToDialogYearFilter()
                with(findNavController()) {
                    currentDestination?.getAction(action.actionId)?.let {
                        navigate(action)
                    }
                }
            }

            MainFeedViewEffect.OpenCountryFilter -> {
                val action = FeedFragmentDirections.actionFragmentFeedToDialogCountryFilter()
                with(findNavController()) {
                    currentDestination?.getAction(action.actionId)?.let {
                        navigate(action)
                    }
                }
            }

            MainFeedViewEffect.OpenGenresFilter -> {
                val action = FeedFragmentDirections.actionFragmentFeedToDialogGenreFilter()
                with(findNavController()) {
                    currentDestination?.getAction(action.actionId)?.let {
                        navigate(action)
                    }
                }

            }

            is MainFeedViewEffect.GoToKinoDetail -> {
                findNavController().navigate("kinoz://kinoDetail/${effect.kinoId}".toUri())
            }
        }
    }

    override fun onDestroyView() {
        binding.rvKinoFeed.adapter = null
        _binding = null
        super.onDestroyView()
    }
}