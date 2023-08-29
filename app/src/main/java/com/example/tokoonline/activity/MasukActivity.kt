package com.example.tokoonline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoonline.R
import com.example.tokoonline.helper.SharedPref
import kotlinx.android.synthetic.main.activity_login.btn_prosesLogin
import kotlinx.android.synthetic.main.activity_loginn.btn_daftarAkun
import kotlinx.android.synthetic.main.activity_register.btn_register

class MasukActivity : AppCompatActivity() {

    lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginn)

        s = SharedPref(this)

         mainButton()
    }

    private fun mainButton(){
        /*btn_prosesLogin.setOnClickListener {
            s.setStatusLogin(true)
        }*/

        btn_daftarAkun.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}