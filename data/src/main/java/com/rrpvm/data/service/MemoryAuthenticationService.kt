package com.rrpvm.data.service

import android.content.Context
import android.util.Log
import com.rrpvm.core.TAG
import com.rrpvm.domain.model.AuthenticationModel
import com.rrpvm.domain.model.UserModel
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject


class MemoryAuthenticationService @Inject constructor(
    private val clientRepository: ClientRepository,
    @ApplicationContext private val context: Context
) : IAuthenticationService {
    private val memoryCoroutineExceptionHandler = CoroutineExceptionHandler { _, cause ->
        Log.e(
            TAG + MemoryAuthenticationService::class.java.name,
            cause.message ?: cause.stackTraceToString()
        )
    }
    private val serviceScope = CoroutineScope(Dispatchers.IO + memoryCoroutineExceptionHandler)
    private val credentials =
        MutableStateFlow<UserModel?>(null)
    private val _currentAccountId = MutableStateFlow<UUID?>(null)
    override val currentAccountId: StateFlow<UUID?>
        get() = _currentAccountId.asStateFlow()
    override val isAuthenticated: StateFlow<Boolean>
        get() = credentials.map {
            it != null
        }.stateIn(serviceScope, SharingStarted.Eagerly, false)

    override suspend fun authenticate(authenticationModel: AuthenticationModel) {
        delay(1000L)
        if (authenticationModel.username.equals("qwe123") && authenticationModel.password.equals("qwe123")) {
            clientRepository.setClientData(ADMIN_MEMORY, ADMIN_MEMORY_INTERNAL_UID).also {
                credentials.value = ADMIN_MEMORY
                _currentAccountId.value = ADMIN_MEMORY_INTERNAL_UID
                setLastInternalUUID(ADMIN_MEMORY_INTERNAL_UID)
            }
        }
    }

    private fun getLastInternalUUID(): UUID? {
        return UUID.fromString(
            context.getSharedPreferences("ff", Context.MODE_PRIVATE).getString("uuid", null)
                ?: return null
        )
    }

    private fun setLastInternalUUID(uuid: UUID) {
        context.getSharedPreferences("ff", Context.MODE_PRIVATE).edit().apply {
            this.putString("uuid", uuid.toString())
            commit()
        }
    }

    init {
        _currentAccountId.value = getLastInternalUUID()
        serviceScope.launch {
            _currentAccountId.collectLatest { accId ->
                if (accId == null) {
                    credentials.value = null
                    return@collectLatest
                }
                credentials.value = runCatching {
                    clientRepository.getClientByInternalUUID(accId)
                }.getOrNull()
            }
        }
    }

    companion object {
        private val ADMIN_MEMORY: UserModel =
            UserModel("UserAdmin", "admin", Calendar.Builder().setDate(2025, 1, 1).build().time)
        private val ADMIN_MEMORY_INTERNAL_UID =
            UUID.fromString("5d145257-8dbf-4312-81cd-4150e7599be4")
    }
}