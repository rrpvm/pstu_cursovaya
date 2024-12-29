package com.rrpvm.core.presentation.mapper

import com.rrpvm.core.presentation.model.UserModelUi
import com.rrpvm.domain.model.UserModel

interface UserModelToUiMapper : UserModel.Mapper<UserModelUi>