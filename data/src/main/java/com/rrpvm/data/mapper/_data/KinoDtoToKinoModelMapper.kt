package com.rrpvm.data.mapper._data

import com.rrpvm.data.mapper_helpers.IsLikedKinoChecker
import com.rrpvm.data.model.kinofilms.KinoModelDto
import com.rrpvm.domain.model.KinoModel
import javax.inject.Inject


class KinoDtoToKinoModelMapper @Inject constructor(private val isLikedKinoChecker: IsLikedKinoChecker) :
    KinoModelDto.Mapper<KinoModel> {
    override fun map(obj: KinoModelDto): KinoModel {
        return KinoModel(
            id = obj.id,
            title = obj.title,
            description = obj.description,
            previewImage = obj.previewImage,
            releasedDate = obj.releasedDate,
            isLiked = isLikedKinoChecker.isLiked(obj.id)
        )
    }
}