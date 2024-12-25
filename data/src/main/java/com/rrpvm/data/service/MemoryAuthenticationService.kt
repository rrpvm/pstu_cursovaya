package com.rrpvm.data.service

import com.rrpvm.data.constants.Constants
import com.rrpvm.domain.model.AuthenticationModel
import com.rrpvm.domain.model.UserModel
import com.rrpvm.domain.service.IAuthenticationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID


class MemoryAuthenticationService : IAuthenticationService {
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val credentials = MutableStateFlow<UserModel?>(null)
    override val isAuthenticated: StateFlow<Boolean>
        get() = credentials.map {
            it != null
        }.stateIn(serviceScope, SharingStarted.Eagerly, false)

    override suspend fun authenticate(authenticationModel: AuthenticationModel) {
        delay(1000L)
        if (authenticationModel.username.equals("qwe123") && authenticationModel.password.equals("qwe123")) {
            val date = "January 2, 2010"
            credentials.value =
                UserModel(
                    UUID.randomUUID().toString(),
                    "admin",
                    Constants.dateFormat.parse(date) ?: throw IllegalArgumentException()
                )
        }
    }
}