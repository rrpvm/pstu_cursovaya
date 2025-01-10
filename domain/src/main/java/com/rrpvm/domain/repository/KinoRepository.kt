package com.rrpvm.domain.repository


import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

//
interface KinoRepository {
    fun getKinoFilmsByDateConstraintSessionDate(minDate: Date,maxDate:Date) : Flow<List<KinoModel>>
    fun getKinoFilmsViewed() : Flow<List<KinoModel>>
    fun getKinoSessions(minDate: Date): Flow<List<KinoSessionModel>>
    fun viewKino(kinoId:String) : Result<Boolean>
    suspend fun fetchKinoFeed(): Result<Boolean>
}