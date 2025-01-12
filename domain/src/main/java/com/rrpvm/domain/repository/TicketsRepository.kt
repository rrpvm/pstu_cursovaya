package com.rrpvm.domain.repository


import com.rrpvm.domain.model.TicketModel
import kotlinx.coroutines.flow.Flow

interface TicketsRepository {
    suspend fun buyTickets(possibleTicketIndexes : List<Int>,sessionId:String)
    fun getTickets() : List<TicketModel>
    fun getTicketsObservable() : Flow<List<TicketModel>>
    fun observeNewTickets() : Flow<TicketModel>
}