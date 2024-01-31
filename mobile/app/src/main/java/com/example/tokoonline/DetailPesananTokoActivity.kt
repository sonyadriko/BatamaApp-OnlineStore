package com.example.tokoonline

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Toko
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.model.firebase.Transaksi
import com.example.tokoonline.databinding.ActivityDetailPesananTokoBinding
import com.example.tokoonline.view.adapter.AdapterListProduk
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.example.tokoonline.view.viewmodel.TokoViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class DetailPesananTokoActivity : BaseActivity() {
    private lateinit var binding : ActivityDetailPesananTokoBinding
    private lateinit var viewModelAlamat : AlamatViewModel
    private lateinit var viewModelToko : TokoViewModel
    val STATUS_PENDING = "pending"
    val STATUS_CANCELED = "canceled"
    val STATUS_SUCCESS = "success"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)
        viewModelToko = ViewModelProvider(this).get(TokoViewModel::class.java)

//        val data = intent?.getSerializableExtra("data") as? Transaction

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        val userId = userRepository.uid.toString()
        val data = intent?.getParcelableExtra<Transaction>("data")
        if (data != null) {
            when (data.status?.toLowerCase()) {
                STATUS_PENDING -> {
                    binding.statusCancel.gone()
                    binding.statusSuccess.gone()
                }
                STATUS_CANCELED -> {
                    binding.statusPending.gone()
                    binding.statusSuccess.gone()
                }
                STATUS_SUCCESS -> {
                    binding.statusPending.gone()
                    binding.statusCancel.gone()
                }
            }
        }
        showAlamatDefault(userId)

        binding.tvMetodePembayaran.visibility = View.VISIBLE
        if (data != null) {
            binding.tvMetodePembayaran.text = "${data.metodePembayaran}"
        }

        binding.tvEstimasi.visible()
        if (data != null) {
            binding.tvEstimasi.text ="${data.metodePengiriman}"
        }
        binding.tvTotalBelanja.visible()
        if (data != null) {
            binding.tvTotalBelanja.text ="${data.harga}"
        }
        binding.tvTotal.visible()
        if (data != null){
            binding.tvTotal.text = "${data.harga.toDouble()}"
        }

        binding.tvAlamatPenjual.visible()
//        if (data != null){
//            binding.tvAlamatPenjual.text = "${data.produkId}"
//        }

        val transactionsReference = FirebaseDatabase.getInstance().getReference("Transactions")
        val productsReference = FirebaseDatabase.getInstance().getReference("Produks")
        val alamatReference = FirebaseDatabase.getInstance().getReference("Alamat")


        val produkId = data?.produkId

        if (produkId != null) {
            // Step 1: Get the sellerId from the Products table using produkId
            productsReference.child(produkId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(productSnapshot: DataSnapshot) {
                    val sellerId = productSnapshot.child("idSeller").getValue(String::class.java)

                    if (sellerId != null) {
                        // Step 2: Get the store details from the Toko table using sellerId
                        val tokoReference = FirebaseDatabase.getInstance().getReference("Toko").child(sellerId)
                        tokoReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (userSnapshot in snapshot.children) {
                                        val userId = userSnapshot.key

                                        for (tokoSnapshot in userSnapshot.children) {
                                            val tokoId = tokoSnapshot.key
                                            val tokoData = tokoSnapshot.getValue(Toko::class.java)
                                            if (tokoData != null) {
                                                binding.tvAlamatPenjual.text = "${tokoData.nama}"
                                            }
                                                    // Lakukan sesuatu dengan data toko yang ditemukan
                                            if (tokoData != null) {
                                                binding.tvAlamatPenjual.text =
                                                    ("Store found for userId: $userId, tokoId: $tokoId")
                                                binding.tvAlamatPenjual.text = ("Nama Toko: ${tokoData.nama}, ID Alamat: ${tokoData.id_alamat}")
                                            }
                                        }
                                    }
                                } else {
                                    binding.tvAlamatPenjual.text = ("No store found for sellerId: $sellerId")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                            })
                    } else {
                        // Handle the case when sellerId is null
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors in fetching Products data
                }
            })
        } else {
            // Handle the case when produkId is null
        }



    }

    @SuppressLint("SetTextI18n")
    private fun showAlamatDefault(userId: String) {
        viewModelAlamat.getAlamatDefault(userId) { alamatDef ->
            with(binding) {
                if (alamatDef != null) {
                    alamatDefault.visible()

                    alamatDefault.text =
                        "${alamatDef.nama} \u2022 ${alamatDef.phone}\n${alamatDef.alamat}"
                } else {

                }
            }
        }
    }
}