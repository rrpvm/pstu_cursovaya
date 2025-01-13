package com.rrpvm.domain.model.kino

import com.rrpvm.domain.model.BaseShortSessionModel

data class KinoWithSessionsModel(
    val kinoModel: KinoModel,
    val sessions: List<BaseShortSessionModel>
)