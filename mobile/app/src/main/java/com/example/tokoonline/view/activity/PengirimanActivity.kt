package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityPengirimanBinding

class PengirimanActivity: BaseActivity() {

    private lateinit var binding: ActivityPengirimanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}