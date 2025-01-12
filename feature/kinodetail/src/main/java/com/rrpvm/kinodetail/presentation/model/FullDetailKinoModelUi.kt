package com.rrpvm.kinodetail.presentation.model

import com.rrpvm.domain.model.AgeRatingModel
import com.rrpvm.domain.model.KinoWithSessionsModel

data class FullDetailKinoModelUi(
    val base: KinoWithSessionsModel,
    val ageRatingModel: AgeRatingModel?,
)