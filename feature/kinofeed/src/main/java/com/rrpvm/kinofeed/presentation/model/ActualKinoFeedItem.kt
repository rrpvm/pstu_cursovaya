package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.KinoModel

data class ActualKinoFeedItem(
    val kinoList: List<KinoModel>,
    val dateMode: PickDateModeUi,
) : FeedItemUi(FeedItemUiTypes.ACTUAL_POSTS) {

}