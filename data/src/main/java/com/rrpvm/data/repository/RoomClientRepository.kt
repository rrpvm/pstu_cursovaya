package com.rrpvm.data.repository

import android.util.Log
import com.rrpvm.core.TAG
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.mapper.UserEntityToUserModelMapper
import com.rrpvm.data.mapper.UserModelToUserEntityMapper
import com.rrpvm.domain.model.UserModel
import com.rrpvm.domain.repository.ClientRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class RoomClientRepository @Inject constructor(
    private val clientDao: ClientDao
) : ClientRepository {
    private val roomCEH = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.i(TAG, "RoomClientRepository: " + throwable.cause)
    }

    override fun getClientFlow(currentUUID: UUID?): Flow<UserModel> {
        return runCatching {
            clientDao.getAuthenticatedUserModel(currentUUID ?: throw NoSuchElementException()).map {
                it.map(UserEntityToUserModelMapper)
            }
        }.getOrElse {
            throw NoSuchElementException()
        }
    }

    override suspend fun getClientInternalUUID(clientId: String): UUID {
        return coroutineScope {
            withContext(this.coroutineContext + roomCEH) {
                runCatching {
                    clientDao.getUserUUID(clientId)
                }.getOrNull() ?: throw NoSuchElementException()
            }
        }
    }

    override suspend fun getClientByInternalUUID(internalUId: UUID): UserModel {
        return coroutineScope {
            withContext(this.coroutineContext + roomCEH) {
                runCatching {
                    clientDao.getUserByUUID(internalUId.toString()).map(UserEntityToUserModelMapper)
                }.getOrNull() ?: throw NoSuchElementException()
            }
        }
    }

    override fun setClientData(userModel: UserModel, accId: UUID) {
        clientDao.setUser(userEntity = userModel.map(UserModelToUserEntityMapper(accId)))
    }


}