package com.rrpvm.kinodetail.presentation.fragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.TAG
import com.rrpvm.domain.model.HallPlaceModel
import com.rrpvm.domain.repository.HallRepository
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketScreenEffect
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

private enum class FetchState {
    IDLE,
    FAILED,
    LOADING,
    SUCCESS
}

private data class HallShortInformation(
    val sessionId: String,
    val hallId: String,
    val hallName: String,
    val rowCount: Int,
    val columnCount: Int,
)

@HiltViewModel
class KinoBuyTicketViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hallRepository: HallRepository
) : ViewModel() {
    private val _screenEffect = MutableSharedFlow<KinoBuyTicketScreenEffect>()
    internal val screenEffect = _screenEffect.asSharedFlow()
    private val fetchState = MutableStateFlow(FetchState.IDLE)
    private val hallFullInformation = MutableStateFlow<HallShortInformation?>(null)
    private val adapterState = MutableStateFlow<List<HallPlaceModel>>(emptyList())

    internal val screenState = combine(
        fetchState,
        adapterState,
        hallFullInformation
    ) { fetchState, adapterState, hallInfo ->
        when (fetchState) {
            FetchState.IDLE, FetchState.LOADING -> {
                return@combine KinoBuyTicketViewState.Loading
            }

            FetchState.FAILED -> {
                return@combine KinoBuyTicketViewState.Failed
            }

            FetchState.SUCCESS -> {
                if (hallInfo == null) return@combine null
                return@combine KinoBuyTicketViewState.Success(
                    sessionId = hallInfo.sessionId,
                    hallId = hallInfo.hallId,
                    places = adapterState,
                    hallName = hallInfo.hallName,
                    rows = hallInfo.rowCount,
                    column = hallInfo.columnCount,
                    selectedPlaces = adapterState.filter { it.isBusyByUser }
                )
            }
        }
    }.filterNotNull()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val sessionId = savedStateHandle.get<String>("sessionId")!!
            fetchState.value = FetchState.LOADING
            hallRepository.getHallInfoBySession(sessionId = sessionId)
                .onSuccess {
                    _screenEffect.emit(
                        KinoBuyTicketScreenEffect.InfoFetched(
                            column = it.columns,
                            row = it.rows
                        )
                    )
                    fetchState.value = FetchState.SUCCESS
                    hallFullInformation.value = HallShortInformation(
                        sessionId = sessionId,
                        hallId = it.hallId,
                        hallName = it.hallName,
                        rowCount = it.rows,
                        columnCount = it.columns
                    )
                    adapterState.value = it.places
                }
                .onFailure {
                    it.printStackTrace()
                }
        }.invokeOnCompletion {
            if (it != null) {
                fetchState.value = FetchState.FAILED
            }
        }

    }

    fun onItemSelected(index: Int) {
        adapterState.value = adapterState.value.toMutableList().apply {
            val place = this[index]
            if (place.isBusyByOther) return
            this[index] = place.copy(isBusyByUser = place.isBusyByUser.not())
        }
    }
}