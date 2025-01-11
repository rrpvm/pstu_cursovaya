package com.rrpvm.kinofeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rrpvm.kinofeed.databinding.BottomDialogFragmentCountryFilterBinding
import com.rrpvm.kinofeed.presentation.adapter.DefaultFilterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryFilterModalBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomDialogFragmentCountryFilterBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: CountryFilterViewModel by viewModels()
    private val adapter by lazy {
        DefaultFilterAdapter(viewModel::onFilterItemStateChanged)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return BottomDialogFragmentCountryFilterBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvCountryFilter.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.genresAdapterList.collectLatest {
                        adapter.setList(it)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding?.rvCountryFilter?.adapter = null
        _binding = null
        super.onDestroyView()
    }
}