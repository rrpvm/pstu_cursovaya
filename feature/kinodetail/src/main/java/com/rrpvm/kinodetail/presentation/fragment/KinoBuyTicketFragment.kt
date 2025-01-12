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
import androidx.recyclerview.widget.GridLayoutManager
import com.rrpvm.kinodetail.R
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
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
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
                binding.nswContent.isVisible = false
                binding.lavCircleLoader.isVisible = false
            }

            KinoBuyTicketViewState.Loading -> {
                binding.nswContent.isVisible = false
                binding.lavCircleLoader.isVisible = true
            }

            is KinoBuyTicketViewState.Success -> {
                binding.nswContent.isVisible = true
                binding.lavCircleLoader.isVisible = false

                uiState.selectedPlaces.takeIf { it.isNotEmpty() }?.let { selectedPlaces ->
                    binding.tvSelectedTicketCount.text =
                        resources.getString(
                            R.string.selected_places,
                            selectedPlaces.size
                        )
                    binding.tvSelectedTicketPlace.text =
                        resources.getString(
                            R.string.selected_places_where,
                            selectedPlaces.map {
                                val column = (it.index % uiState.column).inc()
                                val row = (it.index / uiState.column).inc()
                                "$row ряд $column место, "
                            }.reduce { acc, s -> acc + s }.dropLast(2)
                        )
                    binding.tvTicketPrice.text =
                        resources.getString(R.string.to_pay, selectedPlaces.map {
                            it.price
                        }.reduce { acc, i -> acc + i })
                }.also { isNull ->
                    binding.buyInfoContainer.isVisible = isNull != null
                }

                binding.tvKinoHall.text = uiState.hallName
                adapter.setItems(uiState.places)
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