package top.andnux.little.core.extend

import android.view.ViewGroup

/**
 * 删除子View
 */
fun ViewGroup?.removeAllView() {
    val count = this?.childCount ?: 0
    for (i in 0 until count) {
        val view = this?.getChildAt(i)
        this?.removeView(view)
    }
}