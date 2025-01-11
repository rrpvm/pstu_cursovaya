package com.rrpvm.data.room.entity.query_model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.rrpvm.data.room.entity.KinoGenreCrossRefEntity
import com.rrpvm.data.room.entity.KinoGenreEntity

data class KinoWithSessionsAndGenres(
    @Embedded
    val kinoWithSessions: KinoWithSessions,
    @Relation(
        parentColumn = "kino_id",
        entityColumn = "kino_genre_id",
        entity = KinoGenreEntity::class,
        associateBy = Junction(KinoGenreCrossRefEntity::class)
    )
    val kinoGenres: List<KinoGenreEntity>
){
    interface Mapper<T> {
        fun map(obj: KinoWithSessionsAndGenres): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}