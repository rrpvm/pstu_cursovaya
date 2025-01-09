package com.rrpvm.domain.datasource

import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.model.KinoSessionModel

interface KinofilmsDataSource {
    suspend fun getAllAfishaKinoSessions() : List<KinoSessionModel>
    suspend fun getAllAfishaKinos() : List<KinoModel>
}