package com.rrpvm.kinofeed.presentation.model

import com.rrpvm.domain.model.BaseKinoModel

data class LikedKinoFeedItem(val likedKinoList:List<BaseKinoModel>) : FeedItemUi(FeedItemUiTypes.LIKED_POSTS)