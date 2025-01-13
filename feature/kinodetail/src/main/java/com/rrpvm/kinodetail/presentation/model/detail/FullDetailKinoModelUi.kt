package com.rrpvm.kinodetail.presentation.model.detail

import com.rrpvm.domain.model.AgeRatingModel
import com.rrpvm.domain.model.kino.KinoWithSessionsModel

internal data class FullDetailKinoModelUi(
    val base: KinoWithSessionsModel,
    val ageRatingModel: AgeRatingModel?,
)