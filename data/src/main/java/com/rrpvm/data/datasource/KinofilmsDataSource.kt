package com.rrpvm.data.datasource

import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.domain.model.KinoSessionModel

interface KinofilmsDataSource {
    suspend fun getAllAfishaKinoSessions() : List<KinoSessionModel>
    suspend fun getAllAfishaKinos() : List<KinoModelDto>
}