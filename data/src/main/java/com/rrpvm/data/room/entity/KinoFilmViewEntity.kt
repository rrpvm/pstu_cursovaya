package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "kino_film_views_table", foreignKeys = [ForeignKey(
        entity = KinoEntity::class,
        parentColumns = arrayOf("kino_id"),
        childColumns = arrayOf("viewed_kino_id"),
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("viewed_kino_id", unique = true)]
)
data class KinoFilmViewEntity(
    @PrimaryKey
    @ColumnInfo("view_id")
    val viewId: String,
    @ColumnInfo("viewed_kino_id")
    val kinoFilmId: String
) {
    constructor(kinoId: String) : this(
        viewId = UUID.randomUUID().toString(),
        kinoFilmId = kinoId
    )
}