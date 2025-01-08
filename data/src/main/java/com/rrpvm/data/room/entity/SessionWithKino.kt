package com.rrpvm.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SessionsWithKino(
    @Embedded
    val kinoModel: KinoEntity,
    @Relation(
        parentColumn = "kino_id",
        entity = KinoSessionEntity::class,
        entityColumn = "bound_kino_id"
    )
    val sessionList: List<KinoSessionEntity>

)