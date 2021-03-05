package com.tretiakov.absframework.utils

import android.annotation.SuppressLint
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
        val layout =
            LayoutInflater.from(AbsContext.getInstance().context)
                .inflate(
                    R.layout.abs_toast,
                    null
                )
        val text =
            layout.findViewById<TextView>(R.id.toastTitle)
        if (builder != null) {
            text.text = builder
        } else {
            text.text = msg
        }
        val toast =
            Toast(AbsContext.getInstance().context)
        toast.setGravity(
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            200
        )
        toast.duration = duration
        toast.view = layout
        toast.show()
    }
}