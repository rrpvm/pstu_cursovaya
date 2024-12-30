package com.rrpvm.core.presentation.mapper

import com.rrpvm.core.presentation.Const
import com.rrpvm.core.presentation.model.UserModelUi
import com.rrpvm.domain.model.UserModel

object UserModelToUiMapper : UserModel.Mapper<UserModelUi> {
    override fun map(obj: UserModel): UserModelUi {
        return UserModelUi(
            userName = obj.userName,
            userCreationDate = Const.uiDateFormatter.format(obj.createdDate),
            userAvatar = "https://avatars.mds.yandex.net/i?id=af3b02024107a32573495496fec720b022f077df-5441329-images-thumbs&n=13"
        )
    }
}