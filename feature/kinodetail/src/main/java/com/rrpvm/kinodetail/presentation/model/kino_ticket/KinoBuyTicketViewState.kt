package com.rrpvm.kinodetail.presentation.model.kino_ticket

import com.rrpvm.domain.model.HallPlaceModel

internal sealed class KinoBuyTicketViewState {
    data object Loading : KinoBuyTicketViewState()
    data object Failed : KinoBuyTicketViewState()
    data class Success(
        val places: List<HallPlaceModel>,
        val sessionId: String,
        val rows : Int,
        val column : Int,
        val hallId: String,
        val hallName: String,
        val selectedPlaces : List<HallPlaceModel>
    ) : KinoBuyTicketViewState()

}