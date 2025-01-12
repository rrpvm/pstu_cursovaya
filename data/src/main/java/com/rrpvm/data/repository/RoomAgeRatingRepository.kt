package com.rrpvm.data.repository

import com.rrpvm.data.mapper._entity.AgeRatingEntityToAgeRatingModelMapper
import com.rrpvm.data.room.dao.AgeRatingDao
import com.rrpvm.domain.model.AgeRatingModel
import com.rrpvm.domain.repository.AgeRatingRepository
import javax.inject.Inject

class RoomAgeRatingRepository @Inject constructor(private val ageRatingDao: AgeRatingDao) :
    AgeRatingRepository {
    override fun getAgeRatingById(id: Int): Result<AgeRatingModel> {
        return kotlin.runCatching {
            checkNotNull(ageRatingDao.getAgeRatingById(id)).map(
                AgeRatingEntityToAgeRatingModelMapper
            )
        }
    }
}