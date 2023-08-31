package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.core.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityRegisterBinding
import com.example.tokoonline.data.domain.User
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

        btnLoggiinnn.setOnClickListener{
            goToLoginActivity()
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