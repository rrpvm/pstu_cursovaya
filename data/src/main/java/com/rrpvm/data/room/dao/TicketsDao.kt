package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rrpvm.data.room.entity.TicketEntity

@Dao
interface TicketsDao {
    @Query("SELECT * FROM tickets")
    fun getTickets(): List<TicketEntity>

    @Query("DELETE FROM tickets")
    fun clearTable()

    @Insert(entity = TicketEntity::class)
    fun insertTickets(ticketList:List<TicketEntity>)
}