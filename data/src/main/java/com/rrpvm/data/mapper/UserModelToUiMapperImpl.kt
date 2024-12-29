package com.rrpvm.data.mapper

import com.rrpvm.core.presentation.mapper.UserModelToUiMapper
import com.rrpvm.core.presentation.model.UserModelUi
import com.rrpvm.data.constants.Constants.dateFormat
import com.rrpvm.domain.model.UserModel

class UserModelToUiMapperImpl : UserModelToUiMapper {
    override fun map(obj: UserModel): UserModelUi {
        return UserModelUi(
            obj.userName,
            userCreationDate = dateFormat.format(obj.createdDate),
            userAvatar = null
        )
    }
}