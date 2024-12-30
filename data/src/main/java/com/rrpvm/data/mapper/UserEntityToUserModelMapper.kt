package com.rrpvm.data.mapper

import com.rrpvm.data.room.entity.UserEntity
import com.rrpvm.domain.model.UserModel


object UserEntityToUserModelMapper : UserEntity.Mapper<UserModel> {
    override fun map(obj: UserEntity): UserModel {
        return UserModel(
            userId = obj.userId,
            createdDate = FromDomainDateStringMapper.mapToDomainDate(obj.createdAt),
            userName = obj.username
        )
    }
}