package com.rrpvm.data.room.entity.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoSessionEntity

data class KinoWithSessions(
    @Embedded
    val kinoModel: KinoEntity,
    @Relation(
        parentColumn = "kino_id",
        entity = KinoSessionEntity::class,
        entityColumn = "bound_kino_id"
    )
    val sessionList: List<KinoSessionEntity>
)