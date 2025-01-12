package com.rrpvm.data.model.hall

data class HallInfoDto(
    val hallId: String,
    val hallName: String,
    val rows: Int,
    val columns: Int
)