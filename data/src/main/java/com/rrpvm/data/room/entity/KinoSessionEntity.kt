package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "kino_sessions",
    foreignKeys = [ForeignKey(
        entity = KinoEntity::class,
        parentColumns = arrayOf("kino_id"),
        childColumns = arrayOf("bound_kino_id"),
        onDelete = ForeignKey.CASCADE,

    )]
)
data class KinoSessionEntity(
    @PrimaryKey
    val sessionId: String,
    @ColumnInfo(name = "bound_kino_id", index = true)
    val kinoId: String,
    @ColumnInfo(name = "session_start_date")
    val sessionStartDate: String,
    @ColumnInfo(name = "session_description")
    val sessionDescription: String
) {
    interface Mapper<T> {
        fun map(obj: KinoSessionEntity): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}