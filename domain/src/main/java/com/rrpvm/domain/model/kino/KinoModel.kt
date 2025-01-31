package com.rrpvm.domain.model.kino

import com.rrpvm.domain.model.GenreModel
import java.util.Date

data class KinoModel(
    val id: String,
    val title: String,
    val description: String,
    val previewImage: String,
    val releasedDate: Date,
    val genres: List<GenreModel>,
    val byCountry : String,
    val isLiked: Boolean,
    val ageRatingId : Int,
    val duration : Int//seconds
) {
    interface Mapper<T> {
        fun map(obj: KinoModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}

fun listsEqual(list1: List<KinoModel>, list2: List<KinoModel>): Boolean {

    if (list1.size != list2.size)
        return false

    val pairList = list1.zip(list2)

    return pairList.all { (elt1, elt2) ->
        elt1 == elt2
    }
}