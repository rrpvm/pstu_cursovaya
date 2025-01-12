package com.rrpvm.kinodetail.presentation.fragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.core.TAG
import com.rrpvm.domain.repository.HallRepository
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketPlacesState
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketScreenEffect
import com.rrpvm.kinodetail.presentation.model.kino_ticket.KinoBuyTicketViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KinoBuyTicketViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hallRepository: HallRepository
) : ViewModel() {
    private val _screenState =
        MutableStateFlow<KinoBuyTicketViewState>(KinoBuyTicketViewState.Loading)
    internal val screenState = _screenState.asStateFlow()
    private val _screenEffect = MutableSharedFlow<KinoBuyTicketScreenEffect>()
    internal val screenEffect = _screenEffect.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val sessionId = savedStateHandle.get<String>("sessionId")!!
            hallRepository.getHallInfoBySession(sessionId = sessionId)
                .onSuccess {
                    _screenEffect.emit(KinoBuyTicketScreenEffect.InfoFetched(
                        column = it.columns,
                        row = it.rows
                    ))
                    _screenState.value = KinoBuyTicketViewState.Success(
                        state = KinoBuyTicketPlacesState(
                            sessionId = sessionId,
                            hallId = it.hallId,
                            places = it.places
                        ),
                        hallName = it.hallName
                    )
                    Log.e(TAG, it.toString())
                }
                .onFailure {
                    it.printStackTrace()
                }
        }

    }

    fun onItemSelected(item: Int) {

    }
}