package com.rrpvm.domain.model

data class KinoWithSessionsModel(
    val kinoModel: KinoModel,
    val sessions: List<BaseShortSessionModel>
)