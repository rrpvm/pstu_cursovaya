package com.rrpvm.data.mapper

import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.domain.model.KinoSessionModel

object KinoSessionModelToKinoSessionEntityMapper : KinoSessionModel.Mapper<KinoSessionEntity> {
    override fun map(obj: KinoSessionModel): KinoSessionEntity {
        return KinoSessionEntity(
            sessionId = obj.sessionId,
            kinoId = obj.kinoModel.id,
            sessionStartDate = com.rrpvm.domain.util.Const.baseFullDateFormat.format(obj.sessionStartDate)
        )
    }
}