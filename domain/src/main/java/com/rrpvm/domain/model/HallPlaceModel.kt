package com.rrpvm.domain.model

data class HallPlaceModel(
    val index: Int,
    val price: Int,
    val isBusyByOther: Boolean = false,
    val isBusyByUser: Boolean = false,
) {
    val isBusy get() = isBusyByUser || isBusyByOther
}