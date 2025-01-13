package com.rrpvm.data.mapper._entity

import com.rrpvm.data.mapper.FromDomainDateStringMapper
import com.rrpvm.data.room.entity.KinoSessionEntity
import com.rrpvm.domain.model.BaseShortSessionModel

object KinoSessionEntityToBaseSessionMapper : KinoSessionEntity.Mapper<BaseShortSessionModel> {
    override fun map(obj: KinoSessionEntity): BaseShortSessionModel {
        return BaseShortSessionModel(
            sessionId = obj.sessionId,
            sessionDate = FromDomainDateStringMapper.mapToDomainDate(obj.sessionStartDate),
            sessionInfo = obj.sessionDescription
        )
    }
}