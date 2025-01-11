package com.rrpvm.data.model.kinofilms

import com.rrpvm.domain.model.GenreModel
import java.util.Date

data class KinoModelDto(
    val id: String,
    val title: String,
    val description: String,
    val previewImage: String,
    val releasedDate: Date,
    val byCountry : String,
    val genres : List<GenreModel>,
    val duration : Int = 0//в секундах
){
    interface Mapper<T> {
        fun map(obj: KinoModelDto): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}