package com.rrpvm.data.mapper

import com.rrpvm.data.constants.Constants.dateFormat
import com.rrpvm.data.room.dao.entity.UserEntity
import com.rrpvm.domain.model.UserModel
import java.util.UUID

data class UserModelToUserEntityMapper(val localAccountId: UUID) :
    UserModel.Mapper<UserEntity> {
    override fun map(obj: UserModel): UserEntity {
        return UserEntity(
            internalAccountId = localAccountId,
            userId = obj.userId,
            username = obj.userName,
            createdAt = dateFormat.format(obj.createdDate)
        )
    }
}