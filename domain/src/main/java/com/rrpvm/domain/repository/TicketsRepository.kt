package com.rrpvm.domain.repository


import com.rrpvm.domain.model.TicketModel

interface TicketsRepository {
    suspend fun buyTickets(possibleTicketIndexes : List<Int>,sessionId:String)
    fun getTickets() : List<TicketModel>
}