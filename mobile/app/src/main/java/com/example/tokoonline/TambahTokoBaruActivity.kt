package com.example.tokoonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityTambahTokoBaruBinding
import com.example.tokoonline.databinding.ActivityTokoProfileBinding

class TambahTokoBaruActivity : BaseActivity() {
    private lateinit var binding: ActivityTambahTokoBaruBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahTokoBaruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        initListener()
    }

    private fun initListener() {
        
    }
}