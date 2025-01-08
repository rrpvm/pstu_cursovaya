package com.rrpvm.domain.model

import java.util.Date
import java.util.UUID

data class KinoSessionModel(
    val sessionId : UUID,
    val kinoModel: KinoModel,
    val sessionStartDate: Date
) {
    interface Mapper<T> {
        fun map(obj: KinoSessionModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}