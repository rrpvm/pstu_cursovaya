package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kinos_table")
data class KinoEntity(
    @PrimaryKey
    @ColumnInfo(name = "kino_id")
    val kinoId: String,
    @ColumnInfo(name = "kino_title")
    val mTitle: String,
    @ColumnInfo(name = "kino_description")
    val mDescription: String,
    @ColumnInfo(name = "preview_image")
    val mPreviewImage: String,
    @ColumnInfo(name = "released_date")
    val mReleasedDate: String,
    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean

) {
    interface Mapper<T> {
        fun map(obj: KinoEntity): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}