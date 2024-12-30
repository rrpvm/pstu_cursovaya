package com.rrpvm.core.presentation

import java.text.SimpleDateFormat
import java.util.Locale

object Const {
    private const val UI_DATE_FORMAT = "dd MMMM yyyy"
    val uiDateFormatter = SimpleDateFormat(UI_DATE_FORMAT, Locale("ru"))
}