package com.tretiakov.absframework.abs

import android.content.*
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tretiakov.absframework.routers.AbsCallback
import com.tretiakov.absframework.views.text.AbsTextView

@Suppress("UNCHECKED_CAST")
abstract class KAbsFragment<T>(val layout: Int = 0) : Fragment(), AbsConstants {
    
    private lateinit var activity: AbsActivity

    private var absCallback: AbsCallback<Any>? = null

    private var fragmentFactory: FragmentFactory? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as AbsActivity
        fragmentFactory = requireActivity().supportFragmentManager.fragmentFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layout == 0) return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layout, container, false)
    }

    open fun instance(fClass: Class<out KAbsFragment<T>?>, bundle: Bundle?, absCallback: AbsCallback<Any>?): KAbsFragment<T>? {
        val loader = fClass.classLoader!!
        val name = fClass.name
        val fragment = fragmentFactory!!.instantiate(loader, name) as KAbsFragment<T>
        fragment.arguments = bundle
        fragment.absCallback = absCallback
        return fragment
    }

    protected fun instanceFragment(bundle: Bundle?, router: AbsCallback<Any>): KAbsFragment<T>? {
        arguments = bundle
        setCallback(router)
        return this
    }

    protected open fun instanceFragment(router: AbsCallback<Any>): KAbsFragment<T>? {
        setCallback(router)
        return this
    }

    open fun <T> showUnCancelableDialog(dialog: Class<T>?, bundle: Bundle?, absCallback: AbsCallback<Any>?) {
        if (isVisible && activity != null) {
            activity.showUnCancelableDialog(dialog, bundle, absCallback)
        }
    }

    protected open fun requestPermission(router: AbsCallback<Bundle?>, vararg permissions: String?) {
        activity.requestPermission(router, *permissions)
    }

    open fun setCallback(router: AbsCallback<Any>?) {
        absCallback = router
    }

    protected open fun <T> switchActivity(act: Class<T>,
                                          request: Int, router: AbsCallback<Any>?) {
        activity?.switchActivity(act, Bundle.EMPTY, request, router)
    }

    protected open fun <T> switchActivity(act: Class<T>, bundle: Bundle?,
                                      request: Int, router: AbsCallback<*>?) {
        activity?.switchActivity(act, bundle, request, router)
    }

    protected open fun <T> startActivityAnClearStack(newActivity: Class<T>?) {
        if (activity != null) {
            activity.startActivityAndClearStack(newActivity)
        }
    }

    protected open fun showDialog(dialog: Class<*>, bundle: Bundle?, absCallback: AbsCallback<*>?) {
        if (context == null || !isVisible) return
        val d = AbsDialog.instantiate(requireContext(), dialog.name, bundle) as AbsDialog
        if (absCallback != null) d.setCallback(absCallback)
        d.show((context as AbsActivity?)!!.supportFragmentManager, dialog.name)
    }

    protected open fun showAlertDialog(msg: String, title: String? = null) {
        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(msg)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok))
        { _: DialogInterface?, _: Int -> alertDialog.dismiss() }
        alertDialog.show()
    }

    fun showBottomSheetDialog(dialog: Class<*>, bundle: Bundle, absCallback: AbsCallback<*>?) {
        val d = childFragmentManager.fragmentFactory
                .instantiate(dialog.classLoader!!, dialog.name)
                as AbsBottomSheetDialog

        d.arguments = bundle
        if (absCallback != null) {
            d.setCallback(absCallback as AbsCallback<Any>)
        }
        d.show(childFragmentManager, dialog.name)
    }

    open fun showKFragment(fragment: Fragment, router: AbsCallback<*>) {
        activity?.showKFragment(fragment, Bundle.EMPTY, true, R.id.fragment, router)
    }

    open fun showKFragment(fragment: Fragment, bundle: Bundle, router: AbsCallback<*>) {
        activity?.showKFragment(fragment, bundle, true, R.id.fragment, router)
    }

    protected open fun showFragment(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, absCallback: AbsCallback<Any>?) {
        showFragment(fragment, bundle, addToBackStack, R.id.fragment, absCallback)
    }

    protected open fun showFragment(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, id: Int, absCallback: AbsCallback<Any>?) {
        if (activity != null) activity.showKFragment(fragment, bundle!!, addToBackStack!!, id, absCallback)
    }

    protected open fun addFragment(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, absCallback: AbsCallback<Any>?) {
        addFragment(fragment, bundle, addToBackStack, R.id.fragment, absCallback)
    }

    protected open fun addFragment(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, id: Int, absCallback: AbsCallback<Any>?) {
        if (activity != null) activity.addKotlinFragment(fragment, bundle!!, addToBackStack!!, id, absCallback)
    }

    protected open fun addFragmentRTL(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, id: Int, absCallback: AbsCallback<Any>?) {
        if (activity != null) activity.addKotlinFragment(fragment, bundle!!, addToBackStack!!, id, absCallback)
    }

    protected open fun addFragmentRTL(fragment: KAbsFragment<T>, bundle: Bundle?, addToBackStack: Boolean?, absCallback: AbsCallback<Any>?) {
        addFragmentRTL(fragment, bundle, addToBackStack, R.id.fragment, absCallback)
    }

    protected open fun <D : Any> onData(data: D, goBack: Boolean) {
        if (absCallback != null) {
            absCallback!!.result(data)
        }
        if (goBack) {
            onBackPressed()
        }
    }

    protected open fun onBackPressed() {
        if (activity != null && isVisible) {
            try {
                activity.onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    protected open fun getAction(data: Any?): String {
        return when (data) {
            is String -> data
            is Bundle -> data.getString("action", "")
            else -> throw Exception("Unknown action: $data")
        }
    }

    open fun optColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireContext(), colorRes)
    }

    protected open fun sendLocalBroadcast(intent: Intent?) {
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent!!)
    }

    protected open fun registerLocalBroadcast(receiver: BroadcastReceiver?, vararg actions: String?) {
        val filter = IntentFilter()
        for (action in actions) {
            filter.addAction(action)
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver!!, filter)
    }

    protected open fun unregisterLocalBroadcast(receiver: BroadcastReceiver?) {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver!!)
    }

    protected open fun runOnUiThread(action: Runnable?) {
        activity.runOnUiThread(action)
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
            val window = activity.window

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // finally change the color
            window.statusBarColor = ContextCompat.getColor(activity, color)
        }
    }

    protected open fun setStatusBarAndNavigationColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            val window = activity.window
            setStatusBarColor(color)
            window.navigationBarColor = ContextCompat.getColor(activity,
                    if (color == R.color.abs_color_status_bar) R.color.abs_colorPrimary_V3 else color)
        }
    }

    protected open fun setStatusBarDefaultColor() {
        setStatusBarColor(if (isPre23()) R.color.abs_colorPrimaryDarkPre23 else R.color.abs_color_status_bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && activity != null) {
            val window = activity.window
            window.navigationBarColor = ContextCompat.getColor(activity, R.color.abs_color_status_bar)
        }
    }

    private fun isPre23(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
    }

    protected open fun getAppContext(): Context? {
        return requireContext().applicationContext
    }

    open fun initButtons(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (id in ids) {
            val view = requireView().findViewById<View>(id)
            view?.setOnClickListener(listener)
        }
    }

    open fun <V : View> initUIViews(@IdRes vararg ids: Int): List<V> {
        val result = ArrayList<V>()
        ids.forEach { id -> result.add(requireView().findViewById(id) as V) }
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
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    fun showProgress(view: View) {
        view.visibility = View.VISIBLE
    }

    fun hideProgress(view: View) {
        view.visibility = View.INVISIBLE
    }

    // 0 = type
    // 1 = TextView
    // 2.. = Text
    // last = Color
    fun setTextSpan(vararg args: Any) {

        val builder = SpannableStringBuilder()

        when (args[0]) {
            "auth" -> {
                val text = getString(args[2] as Int).split("|")
                builder.append(text[0])
                builder.append(" ")
                builder.append(text[1])

                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_textSecondary)),
                        0, text[0].length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_colorAccent)),
                        builder.length - text[1].length, builder.length - 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            "accent" -> {
                val title = if (args[2] is String) args[2] as String else getString(args[2] as Int)
                val subtitle = if (args[3] is String) args[3] as String else getString(args[3] as Int)
                builder.append(title)
                builder.append("\n")
                builder.append(subtitle)

                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_colorAccent)),
                        builder.length - subtitle.length, builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            "accent_part_res" -> {
                val text = args[2] as String
                val part = args[3] as String
                builder.append(text)
                val accentStart = text.indexOf(part)

                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_colorAccent)),
                        accentStart, accentStart + part.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            "accent_part" -> {
                var text = args[2] as String
                val accentStart = text.indexOf("{")
                val accentEnd = text.indexOf("}")
                text = text.replace("{", "").replace("}", "")
                builder.append(text)

                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_colorAccent)),
                        accentStart - 1, accentEnd - 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            "subtitle" -> {
                val title = getString(args[2] as Int)
                val subtitle = getString(args[3] as Int)
                builder.append(title)
                builder.append("\n")
                builder.append(subtitle)

                builder.setSpan(
                        RelativeSizeSpan(0.6f),
                        builder.length - subtitle.length, builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setSpan(
                        ForegroundColorSpan(optColor(R.color.abs_textSecondary)),
                        builder.length - subtitle.length, builder.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        val textView = args[1] as AbsTextView
        textView.text = builder
    }


    fun getDisplayWidth(): Int {
        return getDisplaySize().widthPixels
    }

    fun getDisplayHeight(): Int {
        return getDisplaySize().heightPixels
    }

    private fun getDisplaySize(): DisplayMetrics {
        val outMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        return outMetrics
    }

}