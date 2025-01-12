package com.rrpvm.core.presentation.mapper

import com.rrpvm.core.presentation.Const
import com.rrpvm.core.presentation.model.UserModelUi
import com.rrpvm.domain.model.UserModel

object UserModelToUiMapper : UserModel.Mapper<UserModelUi> {
    override fun map(obj: UserModel): UserModelUi {
        return UserModelUi(
            userName = obj.userName,
            userCreationDate = Const.uiDateFormatter.format(obj.createdDate),
            userAvatar = obj.userAvatar
        )
    }
}