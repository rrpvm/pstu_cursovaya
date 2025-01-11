package com.rrpvm.kinodetail.presentation.fragment

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
import com.rrpvm.kinodetail.databinding.FragmentKinoDetailBinding
import com.rrpvm.kinodetail.presentation.adapter.GenreListAdapter
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
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
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

        binding.ivKinoTitle.text = uiState.kino.kinoModel.title

        Glide.with(binding.ivKinoFull)
            .load(uiState.kino.kinoModel.previewImage)
            .centerCrop()
            .into(binding.ivKinoFull)
            .clearOnDetach()
    }

    override fun onDestroyView() {
        binding.rvGenres.adapter = null
        _binding = null
        super.onDestroyView()
    }
}