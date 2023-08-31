package com.example.tokoonline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoonline.R
import com.example.tokoonline.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityLogin2Binding

class Login2Activity : BaseAuthActivity() {

    private lateinit var binding: ActivityLogin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() = with(binding) {
        btnLogin.setOnClickListener{
            if (!edtEmail.text.isNullOrEmpty() && !edtPassword.text.isNullOrEmpty()) {
                showProgressDialog()
                login(edtEmail.text.toString(), edtPassword.text.toString(), doOnFailed = {
                    dismissProgressDialog()
                    showToast("Akun tidak ditemukan")
                })
            }
        }

        btnDaftarAkun.setOnClickListener{
            goToRegister()
        }
    }
}