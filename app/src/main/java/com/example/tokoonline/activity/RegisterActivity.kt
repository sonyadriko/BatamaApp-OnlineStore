package com.example.tokoonline.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityRegisterBinding
import com.example.tokoonline.domain.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

         initListener()
    }

    private fun initListener() = with(binding){
        btnRegister.setOnClickListener{
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val nama = edtNama.text.toString()
            val notelp = edtPhone.text.toString()
            val role = "pembeli"
            val userDomain = User(
                email = email,
                password = password,
                nama = nama,
                noTelepon = notelp,
                role = role
            )

            showProgressDialog()
            register(userDomain = userDomain) {isSuccess ->
                dismissProgressDialog()
                if (isSuccess) onSuccess()
                else showToast("Register gagal")
            }
        }
    }
    private fun onSuccess() {
        lifecycleScope.launch {
            showToast("Register Berhasil")
            delay(500)
            goToLoginActivity()
        }
    }
}