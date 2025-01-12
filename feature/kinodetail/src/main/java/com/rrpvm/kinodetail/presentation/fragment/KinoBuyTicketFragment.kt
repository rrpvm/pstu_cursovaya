package com.rrpvm.kinodetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.rrpvm.kinodetail.databinding.FragmentKinoBuyTicketBinding
import com.rrpvm.kinodetail.presentation.HallPlaceDecorator
import com.rrpvm.kinodetail.presentation.HallSquareGridLayoutManager
import com.rrpvm.kinodetail.presentation.adapter.HallPlaceAdapter
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketScreenEffect
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KinoBuyTicketFragment : Fragment() {
    private var _binding: FragmentKinoBuyTicketBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val viewModel: KinoBuyTicketViewModel by viewModels()
    private val adapter by lazy {
        HallPlaceAdapter(viewModel::onItemSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentKinoBuyTicketBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvPlaces.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.screenState.collectLatest {
                        onUiUpdate(it)
                    }
                }
                launch {
                    viewModel.screenEffect.collectLatest {
                        onUiEffect(it)
                    }
                }
            }
        }
    }

    private fun onUiUpdate(uiState: KinoBuyTicketViewState) {
        when (uiState) {
            KinoBuyTicketViewState.Failed -> {

            }

            KinoBuyTicketViewState.Loading -> {

            }

            is KinoBuyTicketViewState.Success -> {
                binding.tvKinoHall.text = uiState.hallName
                adapter.setItems(uiState.state.places)
            }
        }
    }

    private fun onUiEffect(uiEffect: KinoBuyTicketScreenEffect) {
        when (uiEffect) {
            is KinoBuyTicketScreenEffect.InfoFetched -> {
                binding.rvPlaces.layoutManager =
                    HallSquareGridLayoutManager(binding.rvPlaces.context, uiEffect.column).also {
                        it.orientation = GridLayoutManager.VERTICAL
                    }
                with(binding.rvPlaces) {
                    if (itemDecorationCount > 0) {
                        removeItemDecorationAt(0)
                    }
                    addItemDecoration(HallPlaceDecorator(uiEffect.column, requireContext()), 0)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}