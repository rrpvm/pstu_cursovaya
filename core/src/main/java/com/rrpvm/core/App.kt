package com.rrpvm.core

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

const val TAG = "KinoZ"

fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    resources.displayMetrics
)

fun Resources.toPx(dp: Int): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp.toFloat(),
    displayMetrics
)
fun Resources.toPx(dp: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    displayMetrics
)