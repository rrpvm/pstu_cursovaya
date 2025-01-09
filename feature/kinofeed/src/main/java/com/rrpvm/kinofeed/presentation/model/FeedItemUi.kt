package com.rrpvm.kinofeed.presentation.model

open class FeedItemUi(private val type: FeedItemUiTypes) {
    val mViewType get() = type.viewType
}

enum class FeedItemUiTypes(val viewType: Int) {
    SEEN_POSTS(0xF),
    ACTUAL_POSTS(0xFF),
    NEW_POSTS(0xFFF),
    FATHERLANDS_POSTS(0xFFFF),
    FOREIGN_POSTS(0xFFFFF),

}