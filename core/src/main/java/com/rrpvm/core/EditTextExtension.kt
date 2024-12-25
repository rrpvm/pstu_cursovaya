package com.rrpvm.core

import android.widget.EditText

fun EditText.setSafeText(text: String) {
    if (this.text.toString() == text) return
    this.setText(text)
}