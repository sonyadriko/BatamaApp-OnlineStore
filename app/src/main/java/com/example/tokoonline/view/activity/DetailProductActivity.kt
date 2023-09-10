package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.databinding.ActivityDetailProdukBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailProductActivity: BaseActivity() {
    companion object {
        private const val PRODUK_EXTRA = "extra_produk"
        fun createIntent(context: Context, data: Produk): Intent {
            return Intent(context, DetailProductActivity::class.java).apply {
                putExtra(PRODUK_EXTRA, data)
            }
        }
    }

    private val produkData: Produk? by lazy {
        intent.extras?.parcelable(PRODUK_EXTRA)
    }

    private lateinit var binding: ActivityDetailProdukBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProdukBinding.inflate(layoutInflater)

        verifyProductData()
        initView()
        setContentView(binding.root)
    }

    private fun initView() = with(binding) {
        btnFavorit.setOnClickListener {
            // todo: tambah ke favorit / bookmark
            showToast("Favorited!")
        }

        btnKeranjang.setOnClickListener {
            // TODO: TAMBAH KE KERANJANG
            showToast("berhasil ditambahkan ke keranjang")
        }
    }

    private fun verifyProductData() {
        showProgressDialog()
        lifecycleScope.launch {
            delay(200) // for nicer UI purpose
            if (produkData == null) {
                dismissProgressDialog()
                showDefaultErrorToast()
                finish()
            } else {
                // safe double bang
                showData(produkData!!)
            }
        }
    }

    private fun showData(produkData: Produk) = with(binding) {
        tvNama.text = produkData.nama
        tvHarga.text = moneyFormatter(produkData.harga)
        tvDeskripsi.text = produkData.deskripsi
        Glide.with(image)
            .load(produkData.image.toUri())
            .into(image)

        // dismiss
        dismissProgressDialog()
    }
}