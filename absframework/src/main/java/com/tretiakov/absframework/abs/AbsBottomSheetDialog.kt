package com.tretiakov.absframework.abs

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tretiakov.absframework.R
import com.tretiakov.absframework.constants.AbsConstants
import com.tretiakov.absframework.routers.AbsCallback
import com.tretiakov.absframework.views.text.AbsTextView


/**
 * @author Andrey Tretiakov. Created 4/15/2016.
 */
abstract class AbsBottomSheetDialog : BottomSheetDialogFragment(), AbsConstants {

    var isVisibleLocal = false
        private set

    private var mRouter: AbsCallback<Any>? = null

    protected val handler = Handler(Looper.getMainLooper())

    abstract fun getLayout(): Int

    abstract fun onCreate()

    abstract fun onViewCreated(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(view)
    }

    fun setCallback(absCallback: AbsCallback<Any>) {
        mRouter = absCallback
    }

    override fun onSaveInstanceState(outState: Bundle) {
        isVisibleLocal = false
        super.onSaveInstanceState(outState)
    }

    protected fun switchActivity(activity: Class<*>, bundle: Bundle?,
                                 request: Int, router: AbsCallback<Any>?) {
        (context as AbsActivity).switchActivity(activity, bundle, request, router)
    }

    fun <T> onData(data: T?) {
        mRouter?.result(data)
        close()
    }

    fun <T> onData(data: T?, needDismiss: Boolean) {
        mRouter?.result(data)
        if (needDismiss) {
            close()
        }
    }

    fun <T> onDataAllowingStateLoss(data: T?) {
        mRouter?.result(data)
        dismissAllowingStateLoss()
    }

    private fun close() {
        if (isVisible || isVisibleLocal) {
            dismiss()
        }
    }

    protected fun registerLocalBroadcast(receiver: BroadcastReceiver?, vararg actions: String?) {
        val filter = IntentFilter()
        for (action in actions) {
            filter.addAction(action)
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver!!, filter)
    }

    protected fun unregisterLocalBroadcast(receiver: BroadcastReceiver?) {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver!!)
    }

    protected fun getDimen(@DimenRes dimen: Int): Int {
        return resources.getDimension(dimen).toInt()
    }

    override fun onStart() {
        super.onStart()
        isVisibleLocal = true
    }

    override fun onStop() {
        super.onStop()
        isVisibleLocal = false
    }

    protected fun optColor(@ColorRes color: Int): Int {
        return if (context == null) 0 else ContextCompat.getColor(requireContext(), color)
    }

    protected fun getAction(bundle: Bundle?): String {
        return if (bundle == null) {
            ""
        } else bundle.getString("action", "")
    }



    fun showProgress(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideProgress(view: View) {
        view.visibility = View.INVISIBLE
    }

    protected open fun showDialog(dialog: Class<*>, bundle: Bundle?, absCallback: AbsCallback<*>?) {
        if (context == null || !isVisible) return
        val d = AbsDialog.instantiate(requireContext(), dialog.name, bundle) as AbsDialog
        if (absCallback != null) d.setCallback(absCallback)
        d.show((context as AbsActivity?)!!.supportFragmentManager, dialog.name)
    }
}