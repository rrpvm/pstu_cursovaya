package com.rrpvm.domain.repository


import com.rrpvm.domain.model.BaseKinoModel
import com.rrpvm.domain.model.BaseShortSessionModel
import com.rrpvm.domain.model.GenreModel
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoWithSessionsModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

//
interface KinoRepository {
    fun getKinoFilmsByDateConstraintSessionDate(minDate: Date, maxDate: Date): Flow<List<KinoModel>>
    fun getKinoFilmsViewed(): Flow<List<BaseKinoModel>>
    fun getLikedKinoFilms(): Flow<List<BaseKinoModel>>
    fun getAllKinoFilms(): Flow<List<KinoModel>>
    fun getAllKinoFilmsWithAnyActualSession(): Flow<List<KinoModel>>
    fun viewKino(kinoId: String): Result<Boolean>
    fun getKinoGenres(): Flow<List<GenreModel>>
    fun getKinoById(kinoId: String): Result<KinoWithSessionsModel>
    suspend fun doLike(kinoId: String): Result<KinoModel>
    suspend fun fetchKinoFeed(): Result<Boolean>


    //session repository
    fun getSessionById(sessionId:String) : Result<BaseShortSessionModel>
}