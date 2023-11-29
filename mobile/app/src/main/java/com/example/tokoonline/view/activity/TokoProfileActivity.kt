package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.databinding.ActivityTokoProfileBinding

class TokoProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityTokoProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userRole = userRepository.role


        if (userRole == Constant.Role.PEMBELI) {
            goToTokoSetting()
            finish()
        }
    }

}
