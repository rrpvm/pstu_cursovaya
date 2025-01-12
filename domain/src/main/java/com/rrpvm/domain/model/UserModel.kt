package com.rrpvm.domain.model

import java.util.Date

data class UserModel(
    val userId: String,
    val userName: String,
    val userAvatar :String,
    val createdDate: Date
) {
    interface Mapper<T> {
        fun map(obj: UserModel): T
    }

    fun <T> map(mapper: Mapper<T>): T {
        return mapper.map(this)
    }
}