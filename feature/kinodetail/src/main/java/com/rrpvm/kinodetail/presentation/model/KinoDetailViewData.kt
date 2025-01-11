package com.rrpvm.kinodetail.presentation.model

import com.rrpvm.domain.model.KinoWithSessionsModel

sealed class KinoDetailViewData {
    data object ScreenLoading : KinoDetailViewData()
    data object ScreenFail : KinoDetailViewData()
    data class Success(val kino: KinoWithSessionsModel) : KinoDetailViewData()
}