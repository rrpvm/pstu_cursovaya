package com.rrpvm.data.repository

import com.rrpvm.data.datasource.KinofilmsDataSource
import com.rrpvm.domain.model.SessionHallInfoModel
import com.rrpvm.domain.repository.HallRepository
import javax.inject.Inject

class HallNonCacheRepository @Inject constructor(
    private val dataSource: KinofilmsDataSource
) : HallRepository {
    override suspend fun getHallInfoBySession(sessionId: String): Result<SessionHallInfoModel> {
        return kotlin.runCatching {
            dataSource.getHallInformationBySessionId(sessionId)
        }
    }
}