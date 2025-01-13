package com.rrpvm.data.mapper._entity

import com.rrpvm.data.room.entity.KinoEntity
import com.rrpvm.domain.model.kino.BaseKinoModel

object KinoEntityToBaseKinoModelMapper : KinoEntity.Mapper<BaseKinoModel> {
    override fun map(obj: KinoEntity): BaseKinoModel {
       return BaseKinoModel(
           id = obj.kinoId,
           title = obj.mTitle,
           previewImage = obj.mPreviewImage
       )
    }
}