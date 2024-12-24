package com.rrpvm.data.mapper

import com.rrpvm.data.entity.UserEntity
import com.rrpvm.domain.model.UserModel


object UserEntityToUserModelMapper : UserEntity.Mapper<UserModel> {
    override fun map(obj: UserEntity): UserModel {
        return UserModel(
            userId = obj.userId,
            createdDate = UserEntity.convertDate(obj),
            userName = obj.username
        )
    }
}