package com.tretiakov.absframework.utils

import android.annotation.SuppressLint
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.tretiakov.absframework.R
import com.tretiakov.absframework.context.AbsContext
import me.drakeet.support.toast.ToastCompat

/**
 * @author Andrey Tretiakov created on 01.02.2015.
 */
object Message {
    fun shortToast(msg: String?) {
        showToast(
            msg,
            null,
            Toast.LENGTH_SHORT
        )
    }

    fun shortToast(@StringRes msg: Int) {
        showToast(
            AbsContext.getInstance().context.getString(msg),
            null,
            Toast.LENGTH_SHORT
        )
    }

    fun longToast(@StringRes msg: Int) {
        showToast(
            AbsContext.getInstance().context.getString(msg),
            null,
            Toast.LENGTH_LONG
        )
    }

    fun longToast(msg: String?) {
        showToast(
            msg,
            null,
            Toast.LENGTH_LONG
        )
    }

    fun longToast(builder: SpannableStringBuilder?) {
        showToast(
            null,
            builder,
            Toast.LENGTH_LONG
        )
    }

    @SuppressLint("InflateParams")
    private fun showToast(
        msg: String?,
        builder: SpannableStringBuilder?,
        duration: Int
    ) {
        if (!AbsContext.hasActivities()) {
            return
        }
        if (TextUtils.isEmpty(msg) && builder == null) {
            return
        }

        val toast = ToastCompat.makeText(AbsContext.getInstance().context, builder ?: msg, duration)
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.N_MR1) {
            toast.setBadTokenListener { }
        }
        toast.show()
    }
}