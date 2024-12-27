package com.rrpvm.domain.repository

import com.rrpvm.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.jvm.Throws

interface ClientRepository {
    @Throws(NoSuchElementException::class)
    fun getClientFlow(currentUUID: UUID?): Flow<UserModel>

    @Throws(NoSuchElementException::class)
    suspend fun getClientInternalUUID(clientId: String): UUID

    @Throws(NoSuchElementException::class)
    suspend fun getClientByInternalUUID(internalUId: UUID): UserModel
    fun setClientData(userModel: UserModel, accId: UUID)
}