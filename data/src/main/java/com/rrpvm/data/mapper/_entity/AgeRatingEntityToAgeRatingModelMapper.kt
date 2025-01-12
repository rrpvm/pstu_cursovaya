package com.rrpvm.data.mapper._entity

import com.rrpvm.data.room.entity.AgeRatingEntity
import com.rrpvm.domain.model.AgeRatingModel

object AgeRatingEntityToAgeRatingModelMapper : AgeRatingEntity.Mapper<AgeRatingModel> {
    override fun map(obj: AgeRatingEntity): AgeRatingModel {
        return AgeRatingModel(id = obj.id, urlIcon = obj.iconUrl, minAge = obj.minAge)
    }
}