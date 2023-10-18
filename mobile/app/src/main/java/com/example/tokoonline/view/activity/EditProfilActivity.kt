package com.example.tokoonline.view.activity

import android.os.Bundle
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.databinding.ActivityEditProfilBinding


class EditProfilActivity : BaseActivity() {

    private lateinit var binding: ActivityEditProfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNama.text = userRepository.nama
        binding.tvEmail.text = userRepository.email
        binding.tvPhone.text = userRepository.phone

        binding.btnEditNama.setOnClickListener{
            goToEditProfilForm("formNama")
        }

        binding.btnEditPhone.setOnClickListener{
            goToEditProfilForm("formPhone")
        }


    }
}