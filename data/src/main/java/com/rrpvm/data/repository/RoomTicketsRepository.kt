package com.rrpvm.data.repository

import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.data.room.dao.TicketsDao
import com.rrpvm.data.room.entity.TicketEntity
import com.rrpvm.domain.model.TicketModel
import com.rrpvm.domain.repository.TicketsRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomTicketsRepository @Inject constructor(
    private val ticketsDao: TicketsDao,
    private val dataSource: KinofilmsDataSource,
) : TicketsRepository {
    private val newTicketsFlow =
        MutableSharedFlow<TicketModel>(
            replay = 1,
            extraBufferCapacity = 100,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    override fun observeNewTickets(): Flow<TicketModel> {
        return newTicketsFlow.asSharedFlow()
    }

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

    override fun getTicketsObservable(): Flow<List<TicketModel>> {
        return ticketsDao.getTicketsFlow().map {
            it.map { e ->
                TicketModel(
                    ticketId = e.id,
                    sessionId = e.sessionId,
                    hallId = e.hallId,
                    placeIndex = e.placeIndex
                )
            }
        }
    }

    override suspend fun buyTickets(possibleTicketIndexes: List<Int>, sessionId: String) {
        dataSource.buyTickets(sessionId = sessionId, places = possibleTicketIndexes).onSuccess {
            it.forEach { ticket ->
                newTicketsFlow.emit(ticket)
            }
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