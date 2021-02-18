package com.tretiakov.absframework.utils

import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import org.json.JSONObject


const val TAG = "ABS_UTILS"

fun String.alogD(tag: String = TAG) {
    Log.d(tag, this)
}
fun String.alogI(tag: String = TAG) {
    Log.i(tag, this)
}
fun String.alogE(tag: String = TAG) {
    Log.e(tag, this)
}
fun List<Any>.alogD(tag: String = TAG) {
    Log.d(tag, this.toString())
}
val Int.apx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.adp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
