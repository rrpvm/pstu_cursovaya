package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("age_ratings")
data class AgeRatingEntity(
    @PrimaryKey
    @ColumnInfo("id", index = true)
    val id: Int,

    @ColumnInfo("string_representation")
    val value: String,

    @ColumnInfo("icon_url")
    val iconUrl: String,
    @ColumnInfo("min_age")
    val minAge: Int
) {
    interface Mapper<T> {
        fun map(obj: AgeRatingEntity): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}