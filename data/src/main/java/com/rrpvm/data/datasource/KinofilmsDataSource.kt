package com.rrpvm.data.datasource

import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.domain.model.KinoSessionModel
import com.rrpvm.domain.model.SessionHallInfoModel

interface KinofilmsDataSource {
    suspend fun getAllAfishaKinoSessions() : List<KinoSessionModel>
    suspend fun getAllAfishaKinos() : List<KinoModelDto>
    suspend fun getHallInformationBySessionId(sessionId:String) : SessionHallInfoModel
}