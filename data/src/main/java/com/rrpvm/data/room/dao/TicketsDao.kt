package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rrpvm.data.room.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketsDao {
    @Query("SELECT * FROM tickets")
    fun getTickets(): List<TicketEntity>

    @Query("SELECT * FROM tickets")
    fun getTicketsFlow() : Flow<List<TicketEntity>>

    @Query("DELETE FROM tickets")
    fun clearTable()

    @Insert(entity = TicketEntity::class)
    fun insertTickets(ticketList:List<TicketEntity>)
}