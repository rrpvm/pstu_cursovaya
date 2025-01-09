package com.rrpvm.data.model.kinofilms

import java.util.Date

data class KinoModelDto(
    val id: String,
    val title: String,
    val description: String,
    val previewImage: String,
    val releasedDate: Date,
){
    interface Mapper<T> {
        fun map(obj: KinoModelDto): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}