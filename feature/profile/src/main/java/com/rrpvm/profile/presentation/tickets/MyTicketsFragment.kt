package com.rrpvm.profile.presentation.tickets

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
import com.rrpvm.profile.databinding.FragmentMyTicketsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTicketsFragment : Fragment() {
    private var _binding: FragmentMyTicketsBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: MyTicketViewModel by viewModels()
    private val adapter by lazy {
        MyTicketAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMyTicketsBinding.inflate(
            inflater, container, false
        ).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvTickets.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collectLatest {

                        adapter.setItems(it)
                    }
                }
                launch {
                    viewModel.isLoading.collectLatest {
                        binding.rvTickets.isVisible = it.not()
                        binding.cpiIndicator.isVisible = it
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.rvTickets.adapter = null
        _binding = null
        super.onDestroyView()
    }
}