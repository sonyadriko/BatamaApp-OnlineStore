package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getTotalBelanja
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.databinding.ActivityPembayaranBinding
import com.example.tokoonline.databinding.ActivityPengirimanBinding
import com.example.tokoonline.view.adapter.AdapterItemTransaksi
import com.example.tokoonline.view.viewmodel.AlamatViewModel


class PengirimanActivity : BaseActivity() {
    private lateinit var binding: ActivityPengirimanBinding
    private lateinit var viewModelAlamat: AlamatViewModel

    companion object {
        private const val EXTRA_DATA_PRODUK = "data_produk_extra"
        fun createIntent(context: Context, produkKeranjang: Array<ProdukKeranjang>): Intent {
            return Intent(context, PengirimanActivity::class.java).apply {
                putExtra(EXTRA_DATA_PRODUK, produkKeranjang)
            }
        }
    }

    private val produkKeranjang: Array<ProdukKeranjang>? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelableArray(EXTRA_DATA_PRODUK, ProdukKeranjang::class.java)
        } else {
            intent.extras?.getParcelableArray(EXTRA_DATA_PRODUK)
                ?.map { it as ProdukKeranjang }?.toTypedArray()
        }
    }

    private var metodePengiriman: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelAlamat = ViewModelProvider(this)[AlamatViewModel::class.java]

        val userId = userRepository.uid.toString()

        if (produkKeranjang.isNullOrEmpty()) {
            showToast(getString(R.string.something_wrong))
            finish()
        }

        onResume()
        showAlamatDefault(userId)
        showProdukKeranjang()

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnUbahAlamat.setOnClickListener {
            goToAlamatSetting()
            onPause()
        }

        binding.tvTotalBelanja.text = moneyFormatter(produkKeranjang.getTotalBelanja())
        binding.tvOngkir.text = moneyFormatter(5000)
        binding.tvTotal.text = moneyFormatter(produkKeranjang.getTotalBelanja() + 5000)

        binding.containerMetodePengiriman.setOnCheckedChangeListener { _, i ->
            metodePengiriman = i + 1
        }

        binding.btnBayar.setOnClickListener {
            if (metodePengiriman == null) {
                showToast("tolong pilih metode pengiriman terlebih dahulu")
            } else {
                startActivity(
                    PembayaranActivity.createIntent(this, produkKeranjang!!, metodePengiriman!!)
                )
            }
        }
    }

    private fun showAlamatDefault(userUid: String) {
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

    private fun showProdukKeranjang() {
//        val bundle = intent.extras
//        val produkList = bundle?.getParcelableArrayList<ProdukKeranjang>("produkList")
//        val recyclerView : RecyclerView = binding.rvProdukTransaksi
//        val adapter = AdapterItemTransaksi(produkList)
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
    }
}