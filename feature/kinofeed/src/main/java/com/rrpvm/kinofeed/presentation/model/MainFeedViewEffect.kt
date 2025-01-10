package com.rrpvm.kinofeed.presentation.model

sealed class MainFeedViewEffect  {
    data object OpenGenresFilter : MainFeedViewEffect()
    data object OpenYearFilter : MainFeedViewEffect()
    data object OpenCountryFilter : MainFeedViewEffect()
}