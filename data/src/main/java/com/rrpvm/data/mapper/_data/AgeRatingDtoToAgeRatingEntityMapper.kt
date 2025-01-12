package com.rrpvm.data.mapper._data

import com.rrpvm.data.model.agerating.AgeRatingDto
import com.rrpvm.data.room.entity.AgeRatingEntity

object AgeRatingDtoToAgeRatingEntityMapper : AgeRatingDto.Mapper<AgeRatingEntity> {
    override fun map(obj: AgeRatingDto): AgeRatingEntity {
        return AgeRatingEntity(
            id = obj.id, value = obj.present, iconUrl = obj.iconUrl, minAge = obj.minAge
        )
    }
}