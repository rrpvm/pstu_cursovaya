package com.rrpvm.data.mapper._entity

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.room.entity.query_model.KinoWithSessionsAndGenres
import com.rrpvm.domain.model.BaseShortSessionModel
import com.rrpvm.domain.model.KinoWithSessionsModel

object KinoWithSessionsAndGenresToKinoWithSessionModelMapper :
    KinoWithSessionsAndGenres.Mapper<KinoWithSessionsModel> {
    override fun map(obj: KinoWithSessionsAndGenres): KinoWithSessionsModel {
        return KinoWithSessionsModel(
            kinoModel = obj.map(KinoWithSessionsAndGenresToKinoModel),
            sessions = obj.kinoWithSessions.sessionList.map {
                BaseShortSessionModel(
                    sessionId = it.sessionId,
                    sessionDate = FromDomainDateStringMapper.mapToDomainDate(it.sessionStartDate)
                )
            }
        )
    }
}