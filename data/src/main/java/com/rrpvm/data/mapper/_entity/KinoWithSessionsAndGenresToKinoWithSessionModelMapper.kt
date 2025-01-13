package com.rrpvm.data.mapper._entity

import com.rrpvm.data.room.entity.query_model.KinoWithSessionsAndGenres
import com.rrpvm.domain.model.kino.KinoWithSessionsModel

object KinoWithSessionsAndGenresToKinoWithSessionModelMapper :
    KinoWithSessionsAndGenres.Mapper<KinoWithSessionsModel> {
    override fun map(obj: KinoWithSessionsAndGenres): KinoWithSessionsModel {
        return KinoWithSessionsModel(
            kinoModel = obj.map(KinoWithSessionsAndGenresToKinoModel),
            sessions = obj.kinoWithSessions.sessionList.map {
               it.map(KinoSessionEntityToBaseSessionMapper)
            }
        )
    }
}