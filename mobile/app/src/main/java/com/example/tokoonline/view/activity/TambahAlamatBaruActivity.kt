package com.example.tokoonline.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.firebase.Alamat
import com.example.tokoonline.databinding.ActivityTambahAlamatBaruBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel

class TambahAlamatBaruActivity : BaseActivity() {

    private lateinit var binding: ActivityTambahAlamatBaruBinding
    private val viewModel : AlamatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahAlamatBaruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.btnSimpan.setOnClickListener{
            initListener()
        }
    }

    private fun initListener() = with(binding) {

        val newDataAlamat = Alamat(
            alamat = edtAlamat.text.toString(),
            label = edtType.text.toString(),
            nama = edtNama.text.toString(),
            phone = edtPhone.text.toString(),
            default = false,
            id_users = userRepository.uid.toString(),

            )

        viewModel.addAlamat(newDataAlamat,userUid = userRepository.uid.toString() ) { isSuccess ->
            dismissProgressDialog()
            if (isSuccess) {
                showToast("Alamat berhasil ditambahkan")
                goToAlamatSetting()
            } else {
                showToast("Alamat gagal untuk ditambahkan")
            }
        }
    }

}