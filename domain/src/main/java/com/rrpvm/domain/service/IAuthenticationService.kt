package com.rrpvm.domain.service

import com.rrpvm.domain.model.AuthenticationModel
import kotlinx.coroutines.flow.StateFlow

interface IAuthenticationService {
    val isAuthenticated: StateFlow<Boolean>
    suspend fun authenticate(authenticationModel: AuthenticationModel)
}