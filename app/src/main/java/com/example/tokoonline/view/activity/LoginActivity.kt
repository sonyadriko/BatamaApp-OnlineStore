package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.core.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityLoginBinding

class LoginActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var backClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

    override fun onBackPressed() {
        if (backClicked.not()) {
            showToast("Tekan sekali lagi untuk keluar.")
            backClicked = true
        } else finish()
    }
}