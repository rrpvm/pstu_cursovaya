package com.rrpvm.domain.repository


import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

//
interface KinoRepository {
    fun getKinoFilmsByDateConstraintSessionDate(minDate: Date,maxDate:Date) : Flow<List<KinoModel>>
    fun getKinoFilmsViewed() : Flow<List<KinoModel>>
    fun getAllKinoFilms() : Flow<List<KinoModel>>
    fun viewKino(kinoId:String) : Result<Boolean>
    fun getKinoGenres() : Flow<List<GenreModel>>
    suspend fun fetchKinoFeed(): Result<Boolean>
}