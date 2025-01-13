package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.kino.KinoModel

data class ActualKinoFeedItem(
    val kinoList: List<KinoModel>,
    val dateMode: PickDateModeUi,
) : FeedItemUi(FeedItemUiTypes.ACTUAL_POSTS) {
    enum class Payloads{
        DateModeChanged,
        KinoListChanged
    }
}