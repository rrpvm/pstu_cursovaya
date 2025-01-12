package com.rrpvm.domain.repository

import com.rrpvm.domain.model.SessionHallInfoModel

interface HallRepository {
   suspend  fun getHallInfoBySession(sessionId:String) : Result<SessionHallInfoModel>
}