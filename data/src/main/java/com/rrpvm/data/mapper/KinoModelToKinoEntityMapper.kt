package com.rrpvm.data.mapper

import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.domain.model.KinoModel
import com.rrpvm.domain.util.Const

object KinoModelToKinoEntityMapper : KinoModel.Mapper<KinoEntity> {
    override fun map(obj: KinoModel): KinoEntity {
        return KinoEntity(
            kinoId = obj.id, mTitle = obj.title,
            mDescription = obj.description,
            mPreviewImage = obj.previewImage,
            mReleasedDate = Const.baseFullDateFormat.format(obj.releasedDate),
            isLiked = obj.isLiked,
            mCountry = obj.byCountry
        )
    }
}