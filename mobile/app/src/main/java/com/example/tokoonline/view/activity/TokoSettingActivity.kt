package com.example.tokoonline.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.data.model.firebase.Toko
import com.example.tokoonline.databinding.ActivityTokoSettingBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel

class TokoSettingActivity : BaseActivity() {

    private lateinit var binding: ActivityTokoSettingBinding
    private lateinit var viewModelAlamat : AlamatViewModel
    private lateinit var  viewModelToko : TokoViewModel
    private fun String.toEditable() : Editable {
        return Editable.Factory.getInstance().newEditable(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTokoSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = userRepository.uid.toString()
        val tokoId = intent.getStringExtra("tokoID")
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)
        viewModelToko = ViewModelProvider(this).get(TokoViewModel::class.java)

        if (tokoId != null){
            viewModelToko.getTokoData(userId){toko ->
                val namaToko = toko?.nama ?: ""
                binding.tvNamaToko.text = namaToko.toEditable()
                viewModelAlamat.getAlamatDefault(userId){alamatDefault->
                    val newToko = Toko(
                        id = tokoId,
                        id_users = userRepository.uid.toString(),
                        id_alamat = alamatDefault?.id.toString(),
                        nama = binding.tvNamaToko.text.toString(),
                    )

                    binding.btnSimpanToko.setOnClickListener {
                        showProgressDialog()
                        viewModelToko.updateToko(userId, newToko){isSuccess->
                            if (isSuccess){
                                showToast("Toko Berhasil di Update")
                                dismissProgressDialog()
                                goToTokoProfile()
                            }else{
                                // handle failed to update toko
                            }
                        }
                    }
                }
            }
        }else{
            val intent = Intent(this, TambahTokoBaruActivity::class.java)
            startActivity(intent)
        }

        showAlamatDefault(userId)

        binding.btnUbahAlamat.setOnClickListener{
            goToAlamatSetting()
            finish()
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

//    private fun initListener(tokoId : String){
//        val userId = userRepository.uid.toString()
//        if (tokoId != null){
//            viewModelToko.getTokoById(tokoId, userId){toko->
//                binding.tvNamaToko.text = toko?.nama.toString().toEditable()
//
//            }
//        }else{
//
//        }
//
////        viewModelAlamat.getAlamatDefault(userRepository.uid.toString()) { alamatDefault ->
////            if (alamatDefault != null) {
////                val newToko = Toko(
////                    id_users = userRepository.uid.toString(),
////                    id_alamat = alamatDefault.id.toString(),
////                    nama = binding.tvNamaToko.text.toString(),
////                )
////                viewModelToko.addToko(newToko,userUid = userRepository.uid.toString() ) { isSuccess ->
////                    dismissProgressDialog()
////                    if (isSuccess) {
////                        showToast("Toko berhasil ditambahkan")
////                        goToAlamatSetting()
////                    } else {
////                        showToast("Toko gagal untuk ditambahkan")
////                    }
////                }
////            } else {
////                // Handle if there no alamat default set
////            }
////        }
//    }
}