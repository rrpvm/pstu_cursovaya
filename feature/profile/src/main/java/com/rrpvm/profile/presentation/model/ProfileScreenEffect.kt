package com.rrpvm.profile.presentation.model

sealed class ProfileScreenEffect {
    data object GoMyTickets : ProfileScreenEffect()
}