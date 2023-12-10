package com.example.tokoonline.view.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ActivityPengirimanBinding
import com.example.tokoonline.view.adapter.AdapterItemTransaksi
import com.example.tokoonline.view.viewmodel.AlamatViewModel


class PengirimanActivity : BaseActivity() {
    private lateinit var binding: ActivityPengirimanBinding
    private lateinit var viewModelAlamat : AlamatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this).get(AlamatViewModel::class.java)

        val userId = userRepository.uid.toString()

        onResume()
        showAlamatDefault(userId)
        showProdukKeranjang()


        binding.btnUbahAlamat.setOnClickListener{
            goToAlamatSetting()
            onPause()
        }


        val totalBelanja = intent.getLongExtra("totalBelanja", 0L)

        binding.tvTotalBelanja.text = moneyFormatter(totalBelanja)
        binding.tvOngkir.text = moneyFormatter(5000)
        binding.tvTotal.text = moneyFormatter(totalBelanja + 5000)

        binding.btnBayar.setOnClickListener{

        }

    }

    fun showAlamatDefault(userUid: String) {
        viewModelAlamat.getAlamatDefault(userUid) { alamatDefault ->
            if (alamatDefault != null) {
                binding.tvNamaPenerima.text = alamatDefault.nama
                binding.tvAlamatPenerima.text = alamatDefault.alamat
                binding.tvPhonePenerima.text = alamatDefault.phone
                binding.tvLabelAlamat.text = alamatDefault.label
                binding.tvCatatanAlamat.text = alamatDefault.catatan
                binding.radioButton.isChecked = alamatDefault.default
            } else {
                binding.divAlamat.visibility = View.GONE
            }
        }
    }

    fun showProdukKeranjang(){
        val bundle = intent.extras
        val produkList = bundle?.getParcelableArrayList<ProdukKeranjang>("produkList")
        val recyclerView : RecyclerView = binding.rvProdukTransaksi
        val adapter = AdapterItemTransaksi(produkList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


}