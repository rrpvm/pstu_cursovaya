package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.BaseKinoModel

data class SeenKinoFeedItem(val viewedKinoList:List<BaseKinoModel>) : FeedItemUi(FeedItemUiTypes.SEEN_POSTS)