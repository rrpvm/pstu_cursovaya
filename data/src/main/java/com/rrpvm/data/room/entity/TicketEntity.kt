package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey
    @ColumnInfo(name = "ticket_id")
    val id: String,
    @ColumnInfo(name = "session_id")
    val sessionId: String,
    @ColumnInfo(name = "hall_id")
    val hallId: String,
    @ColumnInfo(name = "place_index")
    val placeIndex: Int
)