package com.rrpvm.kinodetail.presentation.model.kino_ticket

internal sealed class KinoBuyTicketViewState {
    data object Loading : KinoBuyTicketViewState()
    data object Failed : KinoBuyTicketViewState()
    data class Success(
        val state : KinoBuyTicketPlacesState,
        val hallName : String,
    ) : KinoBuyTicketViewState()

}