package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["kino_id", "kino_genre_id"], tableName = "KinoGenreCrossRefEntity")
data class KinoGenreCrossRefEntity(
    @ColumnInfo("kino_id", index = true)
    val kinoId: String,
    @ColumnInfo("kino_genre_id", index = true)
    val genreId: String
)