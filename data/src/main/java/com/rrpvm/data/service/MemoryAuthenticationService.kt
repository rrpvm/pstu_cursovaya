package com.rrpvm.data.service

import android.content.Context
import android.util.Log
import com.rrpvm.core.TAG
import com.rrpvm.data.model.credentials.MemoryCredential
import com.rrpvm.data.model.credentials.MemoryGuestCredential
import com.rrpvm.domain.model.AuthenticationModel
import com.rrpvm.domain.model.Credentials
import com.rrpvm.domain.model.UserModel
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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
    private val credentials = MutableStateFlow<Credentials?>(null)
    private val _currentAccountId = MutableStateFlow(getLastInternalUUID())
    override val currentAccountId: StateFlow<UUID?>
        get() = _currentAccountId.asStateFlow()
    override val isAuthenticated: StateFlow<Boolean>
        get() = credentials.filterNotNull().map {
            it is MemoryCredential || it is MemoryGuestCredential
        }.stateIn(serviceScope, SharingStarted.Eagerly, false)
    override val isAuthJobFirst: Flow<Boolean>
        get() = credentials.map {
            it == null
        }

    override suspend fun authenticate(authenticationModel: AuthenticationModel) {
        delay(1000L)
        if (authenticationModel.username.equals("qwe123") && authenticationModel.password.equals("qwe123")) {
            clientRepository.setClientData(ADMIN_MEMORY, ADMIN_MEMORY_INTERNAL_UID).also {
                credentials.value = MemoryCredential(ADMIN_MEMORY)
                _currentAccountId.value = ADMIN_MEMORY_INTERNAL_UID
            }
        }
    }

    override suspend fun authenticateAsGuest() {
        clientRepository.setClientData(GUEST_MODEL, GUEST_ACC_UID).also {
            credentials.value = MemoryGuestCredential()
            _currentAccountId.value = GUEST_ACC_UID
        }
    }

    override suspend fun logout() {
        delay(500L)
        _currentAccountId.value = null
    }

    private fun getLastInternalUUID(): UUID? {
        return UUID.fromString(
            context.getSharedPreferences("ff", Context.MODE_PRIVATE).getString("uuid", null)
                ?: return null
        )
    }

    private fun setLastInternalUUID(uuid: UUID?) {
        context.getSharedPreferences("ff", Context.MODE_PRIVATE).edit().apply {
            this.putString("uuid", uuid.takeIf { it != null }?.toString())
            commit()
        }
    }

    init {
        serviceScope.launch {
            _currentAccountId.collectLatest { accId ->
                if (accId == GUEST_ACC_UID) {
                    credentials.value = MemoryGuestCredential()
                    return@collectLatest
                }
                setLastInternalUUID(accId)
                if (accId == null) {
                    credentials.value = Credentials.NoAuthCredentials
                    return@collectLatest
                }
                credentials.value = runCatching {
                    delay(1000L)
                    MemoryCredential(clientRepository.getClientByInternalUUID(accId))
                }.getOrDefault(Credentials.NoAuthCredentials)
            }
        }
    }

    companion object {
        private val GUEST_ACC_UID = UUID.fromString("5d145111-8dbf-4312-81cd-4150e751111")
        private val GUEST_MODEL = UserModel(
            userId = "GUEST_ID",
            userName = "Посетитель",
            userAvatar = "https://i.pinimg.com/originals/f0/ca/64/f0ca6421bfb0af3f6ce8cfc03178d9b4.jpg",
            createdDate = Calendar.getInstance().time
        )
        private val ADMIN_MEMORY: UserModel =
            UserModel(
                userId = "UserAdmin",
                userName = "admin",
                userAvatar = "https://avatars.mds.yandex.net/i?id=af3b02024107a32573495496fec720b022f077df-5441329-images-thumbs&n=13",
                createdDate = Calendar.Builder().setDate(2025, 0, 1).build().time
            )
        private val ADMIN_MEMORY_INTERNAL_UID =
            UUID.fromString("5d145257-8dbf-4312-81cd-4150e7599be4")
    }
}