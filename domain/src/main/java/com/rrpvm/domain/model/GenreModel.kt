package com.rrpvm.domain.model

data class GenreModel(
    val genreId: String,
    val title: String
){
    interface Mapper<T> {
        fun map(obj: GenreModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}