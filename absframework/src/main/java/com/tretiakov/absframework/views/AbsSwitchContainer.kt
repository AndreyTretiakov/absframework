package com.tretiakov.absframework.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.appcompat.widget.SwitchCompat
import com.tretiakov.absframework.R
import com.tretiakov.absframework.views.text.AbsTextView

@SuppressLint("CustomViewStyleable")
class AbsSwitchContainer @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyle: Int = 0,
                                                   defStyleRes: Int = 0):
    RelativeLayout(context, attrs, defStyle, defStyleRes) {

    var titleTextView: AbsTextView
    var subtitleTextView: AbsTextView
    var switchView: SwitchCompat

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AbsSwitch)

        val title = a.getText(R.styleable.AbsSwitch__title)
        val subtitle = a.getText(R.styleable.AbsSwitch__subtitle)

        val inflater = LayoutInflater.from(context)
        addView(inflater.inflate(R.layout.abs_switch_content, this, false))

        switchView = findViewById(R.id.switchView)
        val thumbTint = a.getColorStateList(R.styleable.AbsSwitch__thumbColor)
        switchView.thumbTintList = thumbTint
        val trackTint = a.getColorStateList(R.styleable.AbsSwitch__trackColor)
        switchView.trackTintList = trackTint

        titleTextView = findViewById(android.R.id.text1)
        if (title.isNotEmpty()) {
            titleTextView.setTextColor(a.getColor(R.styleable.AbsSwitch__primaryColor, 0))
            titleTextView.text = title
        } else {
            titleTextView.visibility = View.GONE
        }

        subtitleTextView = findViewById(android.R.id.text2)
        if (subtitle.isNotEmpty()) {
            subtitleTextView.setTextColor(a.getColor(R.styleable.AbsSwitch__secondaryColor, 0))
            subtitleTextView.text = subtitle
        } else {
            subtitleTextView.visibility = View.GONE
        }

        a.recycle()
    }

    fun setChecked(value: Boolean) {
        switchView.isChecked = value
    }

    fun isChecked(): Boolean {
        return switchView.isChecked
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        switchView.setOnCheckedChangeListener(listener)
    }

    fun setTitle(titleRes: Int) {
        titleTextView.setText(titleRes)
        if (titleTextView.visibility == View.GONE) {
            titleTextView.visibility = View.VISIBLE
        }
    }

    fun setSubTitle(subTitleRes: Int) {
        subtitleTextView.setText(subTitleRes)
        if (subtitleTextView.visibility == View.GONE) {
            subtitleTextView.visibility = View.VISIBLE
        }
    }


}