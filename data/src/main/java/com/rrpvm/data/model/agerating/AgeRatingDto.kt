package com.rrpvm.data.model.agerating

import com.rrpvm.data.model.kinofilms.KinoModelDto

data class AgeRatingDto(
    val id: Int,
    val present : String,
    val iconUrl : String,
    val minAge : Int
){
    interface Mapper<T> {
        fun map(obj: AgeRatingDto): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}