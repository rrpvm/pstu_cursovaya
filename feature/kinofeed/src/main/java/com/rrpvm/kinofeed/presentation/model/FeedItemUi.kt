package com.rrpvm.kinofeed.presentation.model

open class FeedItemUi(private val type: FeedItemUiTypes) {
    val mViewType get() = type.viewType
}

enum class FeedItemUiTypes(val viewType: Int) {
    SEEN_POSTS(0xF),
    ALL_POSTS(0xFF),
}