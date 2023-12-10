package com.example.tokoonline.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.Toko
import com.example.tokoonline.databinding.ActivityTokoSettingBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class TokoSettingActivity : BaseActivity() {

    private lateinit var binding: ActivityTokoSettingBinding
    private lateinit var viewModelAlamat : AlamatViewModel
    private val viewModelToko : TokoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokoSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = userRepository.uid.toString()
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)


        showAlamatDefault(userId)

        binding.btnUbahAlamat.setOnClickListener{
            goToAlamatSetting()
            finish()
        }

        binding.btnSimpanToko.setOnClickListener{
            initListener()
            goToTokoProfile()
        }



    }

    fun showAlamatDefault(userUid: String) {
        viewModelAlamat.getAlamatDefault(userUid) { alamatDefault ->
            if (alamatDefault != null) {
                binding.tvNamaPenerima.text = alamatDefault.nama
                binding.tvAlamatPenerima.text = alamatDefault.alamat
                binding.tvPhonePenerima.text = alamatDefault.phone
                binding.tvCatatanAlamat.text = alamatDefault.catatan
                binding.radioButton.isChecked = alamatDefault.default
                alamatDefault.id
                binding.divTambahAlamat.visibility = View.GONE
            } else {
                binding.divAlamat.visibility = View.GONE
            }
        }
    }

    private fun initListener(){
        viewModelAlamat.getAlamatDefault(userRepository.uid.toString()) { alamatDefault ->
            if (alamatDefault != null) {
                val newToko = Toko(
                    isSeller = userRepository.uid.toString(),
                    alamat = alamatDefault.id.toString(),
                    nama = binding.tvNamaToko.text.toString(),
                )
                viewModelToko.addToko(newToko,userUid = userRepository.uid.toString() ) { isSuccess ->
                    dismissProgressDialog()
                    if (isSuccess) {
                        showToast("Toko berhasil ditambahkan")
                        goToAlamatSetting()
                    } else {
                        showToast("Toko gagal untuk ditambahkan")
                    }
                }
            } else {
                // Handle if there no alamat default set
            }
        }
    }
}