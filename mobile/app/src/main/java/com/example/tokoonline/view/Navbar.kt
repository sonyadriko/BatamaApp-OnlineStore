package com.example.tokoonline.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.tokoonline.R
import com.example.tokoonline.databinding.NavbarBinding

class Navbar constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    private val binding: NavbarBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = NavbarBinding.inflate(inflater, this, true)

        // Apply custom attributes from XML
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Navbar)
        try {
            // Set left icon
            val leftIconResId = typedArray.getResourceId(R.styleable.Navbar_leftIcon, 0)
            if (leftIconResId != 0) {
                binding.leftIcon.setImageResource(leftIconResId)
            }

            // Set title
            val titleText = typedArray.getString(R.styleable.Navbar_title)
            if (!titleText.isNullOrEmpty()) {
                binding.title.text = titleText
            }

            // Set right icon
            val rightIconResId = typedArray.getResourceId(R.styleable.Navbar_rightIcon, 0)
            if (rightIconResId != 0) {
                binding.rightIcon.setImageResource(rightIconResId)
            }
        } finally {
            typedArray.recycle()
        }
    }

    fun setLeftIcon(resId: Int) {
        binding.leftIcon.setImageResource(resId)
    }

    fun setTitle(text: String) {
        binding.title.text = text
    }

    fun setRightIcon(resId: Int) {
        binding.rightIcon.setImageResource(resId)
    }
}