package com.rrpvm.domain.repository

import com.rrpvm.domain.model.AgeRatingModel

interface  AgeRatingRepository {

    fun getAgeRatingById(id : Int) : Result<AgeRatingModel>
}