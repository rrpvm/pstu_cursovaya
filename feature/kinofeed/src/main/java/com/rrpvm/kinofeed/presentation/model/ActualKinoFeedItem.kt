package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.KinoModel

class ActualKinoFeedItem(
    val kinoList : List<KinoModel>
) : FeedItemUi(FeedItemUiTypes.TODAY_POSTS) {

}