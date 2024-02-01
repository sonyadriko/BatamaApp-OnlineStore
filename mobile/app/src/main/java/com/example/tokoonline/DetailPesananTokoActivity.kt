package com.example.tokoonline

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Toko
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.model.firebase.Transaksi
import com.example.tokoonline.data.repository.firebase.ProdukRepository
import com.example.tokoonline.databinding.ActivityDetailPesananTokoBinding
import com.example.tokoonline.view.adapter.AdapterListProduk
import com.example.tokoonline.view.adapter.AdapterProduk
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

            var idAlamat = data.alamatId
            binding.alamatDefault.text = idAlamat

            viewModelAlamat.getAlamatById(idAlamat, userId){alamatData ->
                if (alamatData !== null){
                    binding.alamatDefault.visible()
                    binding.alamatDefault.text = "${alamatData.nama} \u2022 ${alamatData.phone}\n ${alamatData.alamat}"
                }
            }

            var idProduk = data.produkId
            ProdukRepository.getInstance().getProdukById(idProduk){produk->
                if (produk !== null){
                    binding.tvNama.text = produk.nama
                    binding.tvTotalHarga.text = moneyFormatter(produk.harga)
                    binding.tvWeight.text = produk.beratProduk.toString()
                    Glide.with(this)
                        .load(produk.image)
                        .placeholder(R.drawable.loading_animation) // Placeholder image while loading
                        .into(binding.imgProduk)


                    viewModelToko.getTokoData(produk.idSeller.toString()){toko ->
                        if (toko !== null){
                            viewModelAlamat.getAlamatById(toko.id_alamat, toko.id_users){alamatToko ->
                                if (alamatToko !== null){
                                    binding.tvAlamatPenjual.text = "${toko.nama} \n ${alamatToko.alamat}"
                                }else{
                                    showToast("Gagal mengambil alamat Toko")
                                }
                            }
                        }
                    }
                }
            }



        }

        binding.tvMetodePembayaran.visibility = View.VISIBLE


        if (data != null) {
            binding.tvMetodePembayaran.text = "${data.metodePembayaran}"
        }

        binding.tvEstimasi.visible()
        if (data != null) {
            binding.tvEstimasi.text = "${data.metodePengiriman}"
        }
        binding.tvTotalBelanja.visible()
        if (data != null) {
            binding.tvTotalBelanja.text = "${data.harga}"
        }
        binding.tvTotal.visible()
        if (data != null) {
            binding.tvTotal.text = "${data.harga.toDouble()}"
        }

        binding.tvAlamatPenjual.visible()
//        if (data != null){
//            binding.tvAlamatPenjual.text = "${data.produkId}"
//        }


        val produkId = data?.produkId

        if (produkId !== null){

        }


    }

}