package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.core.base.BaseAuthActivity
import com.example.tokoonline.databinding.ActivityInitialBinding

class InitialActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityInitialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnProsesLogin.setOnClickListener {
            goToLoginActivity()
        }

        binding.btnProsesDaftar.setOnClickListener {
            goToRegister()
        }
    }
}