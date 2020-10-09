package com.tretiakov.absframework.abs

import android.content.*
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tretiakov.absframework.R
import com.tretiakov.absframework.constants.AbsConstants
import com.tretiakov.absframework.routers.Callback
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
abstract class KAbsFragment : Fragment(), AbsConstants {
    
    private var activity: AbsActivity? = null

    private var callback: Callback<Any>? = null

    private var fragmentFactory: FragmentFactory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as AbsActivity
        fragmentFactory = activity!!.supportFragmentManager.fragmentFactory
    }

    open fun instance(fClass: Class<out KAbsFragment?>, bundle: Bundle?, callback: Callback<Any>?): KAbsFragment? {
        val loader = fClass.classLoader!!
        val name = fClass.name
        val fragment = fragmentFactory!!.instantiate(loader, name) as KAbsFragment
        fragment.arguments = bundle
        fragment.callback = callback
        return fragment
    }

    protected fun instanceFragment(bundle: Bundle?, router: Callback<Any>): KAbsFragment? {
        arguments = bundle
        setCallback(router)
        return this
    }

    protected open fun instanceFragment(router: Callback<Any>): KAbsFragment? {
        setCallback(router)
        return this
    }

    open fun <T> showUnCancelableDialog(dialog: Class<T>?, bundle: Bundle?, callback: Callback<Any>?) {
        if (isVisible && activity != null) {
            activity!!.showUnCancelableDialog(dialog, bundle, callback)
        }
    }

    protected open fun requestPermission(router: Callback<Bundle?>, vararg permissions: String?) {
        activity!!.requestPermission(router, *permissions)
    }

    open fun setCallback(router: Callback<Any>?) {
        callback = router
    }

    protected open fun <T> switchActivity(act: Class<T>, bundle: Bundle?,
                                      request: Int, router: Callback<Any>?) {
        if (activity != null) {
            activity!!.switchActivity(act, bundle, request, router)
        }
    }

    protected open fun <T> startActivityAnClearStack(newActivity: Class<T>?) {
        if (activity != null) {
            activity!!.startActivityAndClearStack(newActivity)
        }
    }

    protected open fun <T> showDialog(dialog: Class<T>, bundle: Bundle?, callback: Callback<Any>?): AbsDialog? {
        if (isVisible) {
            val d = AbsDialog.instantiate(context!!, dialog.name, bundle) as AbsDialog
            if (callback != null) d.setCallback(callback)
            if (activity != null && isVisible) {
                try {
                    d.show(childFragmentManager, dialog.name)
                    return d
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return null
        }
        return null
    }

    protected open fun showAlertDialog(msg: String, title: String? = null) {
        val alertDialog = AlertDialog.Builder(activity!!).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok))
        { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }

    protected open fun showFragment(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, callback: Callback<Any>?) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, callback)
    }

    protected open fun showFragment(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, id: Int, callback: Callback<Any>?) {
        if (activity != null) activity!!.showKFragment(fragment, bundle!!, addToBackStack!!, id, callback)
    }

    protected open fun addFragment(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, callback: Callback<Any>?) {
        addFragment(fragment, bundle, addToBackStack, R.id.fragment, callback)
    }

    protected open fun addFragment(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, id: Int, callback: Callback<Any>?) {
        if (activity != null) activity!!.addKFragment(fragment, bundle!!, addToBackStack!!, id, callback)
    }

    protected open fun addFragmentRTL(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, id: Int, callback: Callback<Any>?) {
        if (activity != null) activity!!.addKFragment(fragment, bundle!!, addToBackStack!!, id, callback)
    }

    protected open fun addFragmentRTL(fragment: KAbsFragment, bundle: Bundle?, addToBackStack: Boolean?, callback: Callback<Any>?) {
        addFragmentRTL(fragment, bundle, addToBackStack, R.id.fragment, callback)
    }

    protected open fun <D> onData(data: D, needBack: Boolean) {
        if (callback != null) {
            callback!!.result(data)
        }
        if (needBack) {
            onBackPressed()
        }
    }

    protected open fun onBackPressed() {
        if (activity != null && isVisible) {
            try {
                activity!!.onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected open fun getAction(bundle: Bundle?): String {
        return if (bundle == null) {
            ""
        } else bundle.getString("action", "")
    }

    open fun optColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context!!, colorRes)
    }

    protected open fun sendLocalBroadcast(intent: Intent?) {
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent!!)
    }

    protected open fun registerLocalBroadcast(receiver: BroadcastReceiver?, vararg actions: String?) {
        val filter = IntentFilter()
        for (action in actions) {
            filter.addAction(action)
        }
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiver!!, filter)
    }

    protected open fun unregisterLocalBroadcast(receiver: BroadcastReceiver?) {
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiver!!)
    }

    protected open fun runOnUiThread(action: Runnable?) {
        activity!!.runOnUiThread(action)
    }

    protected open fun addDrawableToLeft(view: TextView, res: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0)
        } else {
            view.setCompoundDrawablesRelativeWithIntrinsicBounds(res, 0, 0, 0)
        }
    }

    protected open fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            val window = activity!!.window

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // finally change the color
            window.statusBarColor = ContextCompat.getColor(activity!!, color)
        }
    }

    protected open fun setStatusBarAndNavigationColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            val window = activity!!.window
            setStatusBarColor(color)
            window.navigationBarColor = ContextCompat.getColor(activity!!,
                    if (color == R.color.abs_color_status_bar) R.color.abs_colorPrimary_V3 else color)
        }
    }

    protected open fun setStatusBarDefaultColor() {
        setStatusBarColor(if (isPre23()) R.color.abs_colorPrimaryDarkPre23 else R.color.abs_color_status_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            val window = activity!!.window
            window.navigationBarColor = ContextCompat.getColor(activity!!, R.color.abs_color_status_bar)
        }
    }

    private fun isPre23(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }

    protected open fun getAppContext(): Context? {
        return context!!.applicationContext
    }

    open fun initButtons(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        val delegate: AppCompatDelegate = activity!!.delegate
        for (id in ids) {
            val view = delegate.findViewById<View>(id)
            view?.setOnClickListener(listener)
        }
    }

    open fun <V : View> initUIViews(@IdRes vararg ids: Int): List<V> {
        val result = ArrayList<V>()
        val delegate: AppCompatDelegate = activity!!.delegate
        ids.forEach { id -> result.add(delegate.findViewById<V>(id) as V) }
        return result
    }

    fun actBundle(action: String, value: String? = null): Bundle {
        val bundle = Bundle()
        bundle.putString("action", action)
        if (value != null) bundle.putString("value", value)
        return bundle
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    fun showProgress(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideProgress(view: View) {
        view.visibility = View.INVISIBLE
    }

}