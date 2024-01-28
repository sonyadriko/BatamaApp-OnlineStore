package com.example.tokoonline.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getTotalBelanja
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.model.midtrans.BillingAddress
import com.example.tokoonline.data.model.midtrans.CustomerDetails
import com.example.tokoonline.data.model.midtrans.ShippingAddress
import com.example.tokoonline.data.repository.firebase.AlamatRepository
import com.example.tokoonline.databinding.ActivityPengirimanBinding
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PengirimanActivity : BaseActivity() {
    private lateinit var binding: ActivityPengirimanBinding
    private lateinit var viewModelAlamat: AlamatViewModel

    private var isAlamatDefaultPresent = false
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

        if (produkKeranjang.isNullOrEmpty()) {
            showToast(getString(R.string.something_wrong))
            finish()
        }

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.editAlamat.setOnClickListener {
            goToAlamatSetting()
            onPause()
        }

        binding.tvTotalBelanja.text = moneyFormatter(produkKeranjang.getTotalBelanja())
        binding.tvOngkir.text = moneyFormatter(5000)
        binding.tvTotal.text = moneyFormatter(produkKeranjang.getTotalBelanja() + 5000)


        binding.btnBayar.setOnClickListener {
            if (metodePengiriman == null) {
                showToast("tolong pilih metode pengiriman terlebih dahulu")
            } else if (isAlamatDefaultPresent) {
                showToast("tambahkan alamat pengiriman terlebih dahulu")
            } else {
                startActivity(
                    PembayaranActivity.createIntent(this, produkKeranjang!!, metodePengiriman!!)
                )
            }
        }

        binding.kirim.setOnClickListener {
            binding.ambil.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.kirim.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }

        binding.ambil.setOnClickListener {
            binding.kirim.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.ambil.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }


        binding.cod.setOnClickListener {
            binding.transfer.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.cod.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }

        binding.transfer.setOnClickListener {
            binding.cod.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.transfer.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAlamatDefault(userUid: String) {
        viewModelAlamat.getAlamatDefault(userUid) { alamatDef ->
            with(binding) {
                if (alamatDef != null) {
                    isAlamatDefaultPresent = true
                    alamatPlaceholder.gone()
                    alamatDefault.visible()

                    alamatDefault.text =
                        "${alamatDef.nama} \u2022 ${alamatDef.phone}\n${alamatDef.alamat}"
                } else isAlamatDefaultPresent = false
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

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        val userId = userRepository.uid.toString()
        showAlamatDefault(userId)
        showProdukKeranjang()
        lifecycleScope.launch {
            delay(200)
            dismissProgressDialog()
        }
    }
}