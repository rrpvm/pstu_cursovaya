package com.rrpvm.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rrpvm.data.constants.Constants.dateFormat
import java.util.Date
import java.util.UUID

@Entity(tableName = "auth_user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val internalAccountId: UUID,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
) {
    interface Mapper<T> {
        fun map(obj: UserEntity): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }

    companion object {
        fun convertDate(entity: UserEntity): Date {
            return dateFormat.parse(entity.createdAt)
                ?: throw IllegalArgumentException("невалидный формат времени")
        }
    }
}
