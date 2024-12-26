package com.rrpvm.data.repository

import android.util.Log
import com.rrpvm.core.TAG
import com.rrpvm.data.room.dao.ClientDao
import com.rrpvm.data.mapper.UserEntityToUserModelMapper
import com.rrpvm.domain.model.UserModel
import com.rrpvm.domain.repository.ClientRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomClientRepository @Inject constructor(
    private val clientDao: ClientDao
) : ClientRepository {
    private val roomCEH = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.i(TAG, "RoomClientRepository: " + throwable.cause)
    }

    override fun getClientFlow(): Flow<UserModel?> {
        return clientDao.getAuthenticatedUserModel().map {
            it?.map(UserEntityToUserModelMapper)
        }
    }

    override suspend fun getClient(): UserModel? {
        return coroutineScope {
            withContext(this.coroutineContext + roomCEH) {
                clientDao.getCurrentUserModel()?.map(UserEntityToUserModelMapper)
            }
        }
    }
}