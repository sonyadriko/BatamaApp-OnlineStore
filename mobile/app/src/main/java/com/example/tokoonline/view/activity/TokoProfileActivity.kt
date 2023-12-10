package com.example.tokoonline.view.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.constanst.Constant
import com.example.tokoonline.databinding.ActivityTokoProfileBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class TokoProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityTokoProfileBinding
    private lateinit var viewModelToko: TokoViewModel
    private lateinit var viewModelAlamat : AlamatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelToko = ViewModelProvider(this).get(TokoViewModel::class.java)
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)


        val userRole = userRepository.role
        if (userRole == Constant.Role.PEMBELI) {
            goToHomepage()
            showToast("Silahkan ajukan permintaan untuk membuka toko pada admin")
            finish()
        }else {
            getTokoData()
        }

    }

    fun getTokoData() {
        val userUid = userRepository.uid.toString()

        viewModelToko.getTokoData(userUid) { toko ->
            if (toko != null) {
                val tokoID = toko.id
                binding.tvNamaToko.text = toko.nama
                binding.tvNamaToko1.text = toko.nama
                binding.tvEmail.text = userRepository.email
                binding.tvPhone.text = userRepository.phone
                viewModelAlamat.getAlamatById(toko.alamat, userUid) { alamatToko ->
                    if (alamatToko != null) {
                        binding.tvAlamatToko.text = alamatToko.alamat
                    } else {
                        showToast("Gagal mengambil Alamat Toko")
                        Log.e("TokoProfileActivity", "Failed to fetch Alamat Toko")
                    }
                }
                binding.optionProduk.setOnClickListener{goToProdukSaya(tokoID)}
            } else {
                showToast("Gagal mengambil Nama Toko")
                Log.e("TokoProfileActivity", "Failed to fetch Toko data")
            }
        }
    }

}
