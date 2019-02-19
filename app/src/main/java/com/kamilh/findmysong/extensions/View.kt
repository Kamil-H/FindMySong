package com.kamilh.findmysong.extensions

import androidx.core.widget.ContentLoadingProgressBar

fun ContentLoadingProgressBar.setShowing(isShowing: Boolean) {
    if (isShowing) show() else hide()
}
