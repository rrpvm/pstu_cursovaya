package com.rrpvm.kinodetail.presentation.model.kino_ticket

internal sealed class KinoBuyTicketScreenEffect {
    data class InfoFetched(val column : Int,val row :Int) : KinoBuyTicketScreenEffect()
    data object GoBackWithSuccess : KinoBuyTicketScreenEffect()
    data object GoBackWithFailure : KinoBuyTicketScreenEffect()
}