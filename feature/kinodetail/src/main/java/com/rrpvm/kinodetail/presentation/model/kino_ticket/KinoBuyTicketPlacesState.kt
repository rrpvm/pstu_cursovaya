package com.rrpvm.kinodetail.presentation.model.kino_ticket

import com.rrpvm.domain.model.HallPlaceModel

internal data class KinoBuyTicketPlacesState(
    val sessionId: String,
    val hallId: String,
    val places: List<HallPlaceModel>
)