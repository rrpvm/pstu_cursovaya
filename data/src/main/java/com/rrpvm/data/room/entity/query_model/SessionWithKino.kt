package com.rrpvm.data.room.entity.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.data.room.entity.KinoSessionEntity

data class SessionWithKino(
    @Embedded
    val sessionEntity: KinoSessionEntity,
    @Relation(parentColumn = "bound_kino_id", entityColumn = "kino_id")
    val kinoEntity: KinoEntity
)