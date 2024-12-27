package com.rrpvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rrpvm.data.room.dao.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ClientDao {
    @Query("SELECT * FROM auth_user WHERE id=:uuid")
    fun getAuthenticatedUserModel(uuid : UUID) : Flow<UserEntity>
    @Query("SELECT * FROM auth_user WHERE id=:UUID")
    fun getUserByUUID(UUID:String) : UserEntity
    @Query("SELECT id FROM auth_user WHERE user_id=:userId")
    fun getUserUUID(userId:String) : UUID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setUser(userEntity: UserEntity)
}