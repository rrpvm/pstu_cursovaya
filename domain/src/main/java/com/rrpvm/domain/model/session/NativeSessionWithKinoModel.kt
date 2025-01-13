package com.rrpvm.domain.model.session

import com.rrpvm.domain.model.BaseShortSessionModel
import com.rrpvm.domain.model.kino.BaseKinoModel

class NativeSessionWithKinoModel(
    val baseSessionInfo: BaseShortSessionModel,
    val baseKinoInfo : BaseKinoModel
)