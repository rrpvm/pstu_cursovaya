package com.rrpvm.data.repository

import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.room.dao.TicketsDao
import com.rrpvm.data.room.entity.TicketEntity
import com.rrpvm.domain.model.TicketModel
import com.rrpvm.domain.repository.TicketsRepository
import javax.inject.Inject

class RoomTicketsRepository @Inject constructor(
    private val ticketsDao: TicketsDao,
    private val dataSource: KinofilmsDataSource,
) : TicketsRepository {
    override fun getTickets(): List<TicketModel> {
        return ticketsDao.getTickets().map {
            TicketModel(
                ticketId = it.id,
                sessionId = it.sessionId,
                hallId = it.hallId,
                placeIndex = it.placeIndex
            )
        }
    }

    override suspend fun buyTickets(possibleTicketIndexes: List<Int>, sessionId: String) {
        dataSource.buyTickets(sessionId = sessionId, places = possibleTicketIndexes).onSuccess {
            ticketsDao.insertTickets(ticketList = it.map {
                TicketEntity(
                    id = it.ticketId,
                    sessionId = it.sessionId,
                    hallId = it.hallId,
                    placeIndex = it.placeIndex
                )
            })
        }.onFailure {
            throw it
        }
    }
}