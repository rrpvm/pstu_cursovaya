package com.rrpvm.data.mapper

import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.domain.model.KinoModel

object KinoEntityToModelMapper : KinoEntity.Mapper<KinoModel> {
    override fun map(obj: KinoEntity): KinoModel {
        return KinoModel(
            id = obj.kinoId,
            title = obj.mTitle,
            description = obj.mDescription,
            previewImage = obj.mPreviewImage,
            releasedDate = FromDomainDateStringMapper.mapToDomainDate(obj.mReleasedDate),
            isLiked = obj.isLiked
        )
    }
}