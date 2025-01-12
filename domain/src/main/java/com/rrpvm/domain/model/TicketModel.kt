package com.rrpvm.domain.model

data class TicketModel(
    val ticketId: String,
    val sessionId: String,
    val hallId: String,
    val placeIndex: Int
)