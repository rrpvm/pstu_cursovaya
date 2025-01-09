package com.rrpvm.kinofeed.presentation.model

import androidx.annotation.StringRes
import com.rrpvm.kinofeed.R

enum class  PickDateModeUi(@StringRes val resId : Int) {
    TODAY(R.string.pick_mode_today),
    TOMORROW(R.string.pick_mode_tomorrow),
    AT_WEEK(R.string.pick_mode_week)
}