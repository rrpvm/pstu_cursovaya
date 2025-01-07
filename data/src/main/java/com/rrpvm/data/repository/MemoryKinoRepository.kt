package com.rrpvm.data.repository

import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.repository.KinoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MemoryKinoRepository @Inject constructor(

) : KinoRepository {

    override fun getKinoFeed(): Flow<List<KinoModel>> {
        return MutableStateFlow(listOf())
    }

    override suspend fun fetchKinoFeed(): Result<Boolean> {
        return Result.failure(IllegalArgumentException())
    }
}