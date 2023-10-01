package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseAuthActivity
import com.example.tokoonline.core.util.SharedPref

class InitialActivity : BaseAuthActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        s = SharedPref(this)
    }
}