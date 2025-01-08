package com.rrpvm.domain.repository

import com.rrpvm.domain.model.KinoSessionModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

//
interface KinoRepository {
    fun getKinoSessions(minDate: Date): Flow<List<KinoSessionModel>>
    suspend fun fetchKinoFeed(): Result<Boolean>
}