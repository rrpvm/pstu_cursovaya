package com.rrpvm.domain.model

import java.util.Date

data class KinoSessionModel(
    val sessionId : String,
    val kinoModel: KinoModel,
    val sessionStartDate: Date,
    val hallName : String
) {
    interface Mapper<T> {
        fun map(obj: KinoSessionModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}