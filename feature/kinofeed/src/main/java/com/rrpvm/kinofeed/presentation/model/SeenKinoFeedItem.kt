package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.KinoModel

data class SeenKinoFeedItem(val viewedKinoList:List<KinoModel>) : FeedItemUi(FeedItemUiTypes.SEEN_POSTS)