package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.Alamat
import com.example.tokoonline.databinding.ActivityAlamatFormBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.google.firebase.storage.StorageReference

class AlamatFormActivity : BaseActivity() {
    private lateinit var binding: ActivityAlamatFormBinding
    private val viewModel : AlamatViewModel by viewModels()
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlamatFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSimpanAlamat.setOnClickListener{
            initListener()
        }

    }
    private fun initListener() = with(binding){
        showProgressDialog()

        val dataAlamatNew = Alamat(
            label = tvLabelAlamat.text.toString(),
            alamat = tvAlamatPenerima.text.toString(),
            catatan = tvCatatanAlamat.text.toString(),
            nama = tvNamaPenerima.text.toString(),
            phone = tvPhonePenerima.text.toString(),
            id_users = userRepository.uid
        )
        viewModel.addAlamat(dataAlamatNew){isSuccess ->
            dismissProgressDialog()
            if (isSuccess){
                showToast("Alamat berhasil ditambahkan")
            }else showToast("Failed")
        }
    }
}
