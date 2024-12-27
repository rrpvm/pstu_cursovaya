package com.rrpvm.domain.service

import com.rrpvm.domain.model.AuthenticationModel
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

interface IAuthenticationService {
    val isAuthenticated: StateFlow<Boolean>
    val currentAccountId: StateFlow<UUID?>
    suspend fun authenticate(authenticationModel: AuthenticationModel)
}