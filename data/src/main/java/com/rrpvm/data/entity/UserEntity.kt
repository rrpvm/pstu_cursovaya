package com.rrpvm.data.entity

import androidx.room.Entity
import com.rrpvm.data.constants.Constants.dateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class UserEntity(
    internal val password: String,
    val username: String,
    val userId: String,
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
