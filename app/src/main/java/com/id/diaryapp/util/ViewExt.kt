package com.id.diaryapp.util

import android.view.View
import androidx.core.view.isGone

fun View.gone() {
    isGone = true
}

fun View.visible() {
    isGone = false
}