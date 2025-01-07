package com.rrpvm.domain.repository

import com.rrpvm.domain.model.KinoModel
import kotlinx.coroutines.flow.Flow

//
interface KinoRepository {
    fun getKinoFeed() : Flow<List<KinoModel>>
    suspend fun fetchKinoFeed() : Result<Boolean>
}