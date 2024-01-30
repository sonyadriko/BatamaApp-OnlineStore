package com.example.tokoonline.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
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

        val emptyView: LinearLayout = binding.tokoNull
        val viewOn: LinearLayout = binding.viewOnToko


        
//        val userRole = userRepository.role
        val userId = userRepository.uid

        if (userId != null) {
            viewModelToko.checkUserHasToko(userId) { userHasToko ->
                if (userHasToko) {
                    // User has a toko, show toko details
                    emptyView.visibility = View.GONE
                    viewOn.visibility = View.VISIBLE

                    binding.toolbar.binding.leftIcon.setOnClickListener {
                        finish()
                    }
                    getTokoData()


                    // Use tokoData to populate your views
                    // Example: binding.tvNamaToko.text = tokoData.nama
                } else {
                    // User doesn't have a toko, show button to add a new toko
                    emptyView.visibility = View.VISIBLE
                    viewOn.visibility = View.GONE

                    binding.toolbarNull.binding.leftIcon.setOnClickListener {
                        finish()
                    }
                    binding.btnTambahToko.setOnClickListener {
                        // Handle the case for adding a new toko
                        val intent = Intent(this, TambahTokoBaruActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

//        if (userRole == Constant.Role.PEMBELI) {
//            emptyView.visibility = View.VISIBLE
//            viewOn.visibility = View.GONE
//            binding.toolbarNull.binding.leftIcon.setOnClickListener {
//                finish()
//            }
//            binding.btnTambahToko.setOnClickListener {
//                val intent = Intent(this, TambahTokoBaruActivity::class.java)
//                startActivity(intent)
//            }
//
//        }else {
//            emptyView.visibility = View.GONE
//            viewOn.visibility = View.VISIBLE
//
//            binding.toolbar.binding.leftIcon.setOnClickListener {
//                finish()
//            }
//            getTokoData()
//        }

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
                viewModelAlamat.getAlamatById(toko.id_alamat, userUid) { alamatToko ->
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
