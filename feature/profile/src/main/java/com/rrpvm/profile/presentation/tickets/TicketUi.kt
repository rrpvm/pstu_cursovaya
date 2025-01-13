package com.rrpvm.profile.presentation.tickets

data class TicketUi(
    val sessionStart: String,
    val place: Int,
    val placeRow: Int,
    val placeColumn: Int,
    val kinoTitle: String,
    val hallName:String,
)