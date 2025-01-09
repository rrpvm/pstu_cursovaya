package com.rrpvm.kinofeed.presentation

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
import com.rrpvm.kinofeed.databinding.FragmentKinoFeedBinding
import com.rrpvm.kinofeed.presentation.adapter.KinoFeedAdapter
import com.rrpvm.kinofeed.presentation.model.FeedItemUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private var _binding: FragmentKinoFeedBinding? = null
    private val binding get() = checkNotNull(_binding)
    private var mAdapter: KinoFeedAdapter? = null
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = KinoFeedAdapter()
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
        super.onViewCreated(view, savedInstanceState)
        binding.rvKinoFeed.adapter = mAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.mAdapterLoadingState.collectLatest { isLoading ->
                        binding.llMainProgressContainer.isVisible = isLoading
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

    override fun onDestroyView() {
        binding.rvKinoFeed.adapter = null
        _binding = null
        super.onDestroyView()
    }
}