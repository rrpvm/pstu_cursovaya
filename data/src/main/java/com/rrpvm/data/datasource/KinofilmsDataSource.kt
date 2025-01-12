package com.rrpvm.data.datasource

import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.domain.model.KinoSessionModel
import com.rrpvm.domain.model.SessionHallInfoModel
import com.rrpvm.domain.model.TicketModel

interface KinofilmsDataSource {
    suspend fun getAllAfishaKinoSessions() : List<KinoSessionModel>
    suspend fun getAllAfishaKinos() : List<KinoModelDto>
    suspend fun getHallInformationBySessionId(sessionId:String) : SessionHallInfoModel
    suspend fun buyTickets(sessionId: String, places:List<Int>) : Result<List<TicketModel>>
}