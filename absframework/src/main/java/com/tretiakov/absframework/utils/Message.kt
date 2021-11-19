package com.tretiakov.absframework.utils

import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.tretiakov.absframework.context.AbsContext

/**
 * @author Andrey Tretiakov created on 01.02.2015.
 */
object Message {

    @JvmStatic
    fun shortToast(msg: String?) {
        showToast(msg, null, Toast.LENGTH_SHORT)
    }

    @JvmStatic
    fun shortToast(@StringRes msg: Int) {
        showToast(AbsContext.getInstance().context.getString(msg), null, Toast.LENGTH_SHORT)
    }

    @JvmStatic
    fun longToast(@StringRes msg: Int) {
        showToast(AbsContext.getInstance().context.getString(msg), null, Toast.LENGTH_LONG)
    }

    @JvmStatic
    fun longToast(msg: String?) {
        showToast(msg, null, Toast.LENGTH_LONG)
    }

    @JvmStatic
    fun longToast(builder: SpannableStringBuilder?) {
        showToast(null, builder, Toast.LENGTH_LONG)
    }

    private fun showToast(msg: String?, builder: SpannableStringBuilder?, duration: Int) {
        if (!AbsContext.hasActivities() || (TextUtils.isEmpty(msg) && builder == null)) {
            return
        }

        val context = AbsContext.getInstance().context
        val text = msg ?: builder.toString()
        Handler(context.mainLooper).post {
            Toast.makeText(context, text, duration).show()
        }
    }
}