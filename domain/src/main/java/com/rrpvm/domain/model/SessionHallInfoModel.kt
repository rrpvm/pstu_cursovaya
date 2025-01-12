package com.rrpvm.domain.model

data class SessionHallInfoModel(
    val hallId: String,
    val hallName : String,
    //owner(session)
    val ownerId: String,
    val rows: Int,
    val columns: Int,
    val placesSize: Int,
    val places: List<HallPlaceModel>
)