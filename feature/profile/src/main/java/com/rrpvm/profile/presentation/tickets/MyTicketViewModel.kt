package com.rrpvm.profile.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.repository.HallRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.domain.repository.TicketsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MyTicketViewModel @Inject constructor(
    ticketsRepository: TicketsRepository,
    kinoRepository: KinoRepository,
    hallRepository: HallRepository
) : ViewModel() {
    private val _state = MutableStateFlow<List<TicketUi>>(emptyList())
    val state = _state.asStateFlow()
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ticketsRepository.getTicketsObservable().map {
                _isLoading.value = true
                it.mapNotNull { ticket ->
                    val hallInfo = hallRepository.getHallInfoBySession(sessionId = ticket.sessionId)
                        .onFailure {
                            it.printStackTrace()
                        }
                        .getOrNull() ?: return@mapNotNull null
                    val kinoWithSession =
                        kinoRepository.getSessionWithKinoBySessionId(ticket.sessionId).onFailure {
                            it.printStackTrace()
                        }.getOrNull() ?: return@mapNotNull null
                    val column = (ticket.placeIndex % hallInfo.columns).inc()
                    val row = (ticket.placeIndex / hallInfo.columns).inc()
                    return@mapNotNull TicketUi(
                        sessionStart = dateFormat.format(kinoWithSession.baseSessionInfo.sessionDate),
                        place = ticket.placeIndex + 1,
                        placeRow = row,
                        placeColumn = column,
                        kinoTitle = kinoWithSession.baseKinoInfo.title,
                        hallName = kinoWithSession.baseSessionInfo.sessionInfo
                    )
                }
            }.catch {
                it.printStackTrace()
                _isLoading.value = false
            }.onEach {
                _isLoading.value = false
                _state.value = it
            }.launchIn(this)
        }
    }

    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM/2025, в HH:mm", Locale("Ru"))
    }
}