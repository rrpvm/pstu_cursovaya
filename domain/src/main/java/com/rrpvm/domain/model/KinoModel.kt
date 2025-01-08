package com.rrpvm.domain.model

import java.util.Date
import java.util.UUID

data class KinoModel(
    val id: UUID,
    val title: String,
    val description: String,
    val previewImage: String,
    val releasedDate: Date,
    val isLiked: Boolean = false
){
    interface Mapper<T> {
        fun map(obj: KinoModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}