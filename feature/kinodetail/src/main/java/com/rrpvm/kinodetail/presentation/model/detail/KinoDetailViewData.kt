package com.rrpvm.kinodetail.presentation.model.detail



internal sealed class KinoDetailViewData {
    data object ScreenLoading : KinoDetailViewData()
    data object ScreenFail : KinoDetailViewData()
    data class Success(val kino: FullDetailKinoModelUi) : KinoDetailViewData()
}