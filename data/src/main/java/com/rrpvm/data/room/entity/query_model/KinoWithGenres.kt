package com.rrpvm.data.room.entity.query_model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoGenreCrossRefEntity
import com.rrpvm.data.room.entity.KinoGenreEntity


data class KinoWithGenres(
    @Embedded val kino: KinoEntity,
    @Relation(
        parentColumn = "kino_id",
        entityColumn = "kino_genre_id",
        associateBy = Junction(KinoGenreCrossRefEntity::class)
    )
    val genres: List<KinoGenreEntity>
){
    interface Mapper<T> {
        fun map(obj: KinoWithGenres): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}