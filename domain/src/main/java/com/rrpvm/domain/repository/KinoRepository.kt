package com.rrpvm.domain.repository


import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoWithSessionsModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

//
interface KinoRepository {
    fun getKinoFilmsByDateConstraintSessionDate(minDate: Date, maxDate: Date): Flow<List<KinoModel>>
    fun getKinoFilmsViewed(): Flow<List<KinoModel>>
    fun getAllKinoFilms(): Flow<List<KinoModel>>
    fun getAllKinoFilmsWithAnyActualSession(): Flow<List<KinoModel>>
    fun viewKino(kinoId: String): Result<Boolean>
    suspend fun doLike(kinoId: String):Result<KinoModel>
    fun getKinoGenres(): Flow<List<GenreModel>>
    fun getKinoById(kinoId: String): Result<KinoWithSessionsModel>
    suspend fun fetchKinoFeed(): Result<Boolean>
}