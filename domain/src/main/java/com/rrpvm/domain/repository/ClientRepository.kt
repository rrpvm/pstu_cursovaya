package com.rrpvm.domain.repository

import com.rrpvm.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
     fun getClientFlow() : Flow<UserModel?>
    suspend fun getClient() : UserModel?
}