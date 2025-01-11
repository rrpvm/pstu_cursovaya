package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kino_genre")
data class KinoGenreEntity(
    @PrimaryKey
    @ColumnInfo("kino_genre_id")
    val mGenreId: String,
    @ColumnInfo("genre_name")
    val mGenreName: String
) {
    interface Mapper<T> {
        fun map(obj: KinoGenreEntity): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}