package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.core.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListener()
    }

    private fun initListener() = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        btnLogin.setOnClickListener{
            if (!edtEmail.text.isNullOrEmpty() && !edtPassword.text.isNullOrEmpty()) {
                showProgressDialog()
                login(edtEmail.text.toString(), edtPassword.text.toString(), doOnFailed = {
                    lifecycleScope.launch {
                        delay(500)
                        dismissProgressDialog()
                        showToast("Akun tidak ditemukan")
                    }
                })
            }
        }

        btnDaftarAkun.setOnClickListener{
            goToRegister()
        }
    }
}