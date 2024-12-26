package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.rrpvm.data.room.dao.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM auth_user WHERE id=255")
    fun getAuthenticatedUserModel() : Flow<UserEntity?>
    @Query("SELECT * FROM auth_user WHERE id=255")
    fun getCurrentUserModel() : UserEntity?
}