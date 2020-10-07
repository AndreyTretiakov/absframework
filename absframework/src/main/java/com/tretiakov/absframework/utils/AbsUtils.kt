package com.tretiakov.absframework.utils

import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import org.json.JSONObject


const val TAG = "ABS_UTILS"

fun String.logD(tag: String = TAG) {
    Log.d(tag, this)
}
fun String.logI(tag: String = TAG) {
    Log.i(tag, this)
}
fun String.logE(tag: String = TAG) {
    Log.e(tag, this)
}
fun List<Any>.logD(tag: String = TAG) {
    Log.d(tag, this.toString())
}
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
