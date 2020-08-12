package com.lbz.googlearchitecture.utils

import android.text.Html
import android.text.Spanned

/**
 * @author: laibinzhi
 * @date: 2020-08-05 11:48
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}