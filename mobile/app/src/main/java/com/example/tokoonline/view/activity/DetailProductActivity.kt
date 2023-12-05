package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.Produk
import com.example.tokoonline.data.repository.KeranjangRepository
import com.example.tokoonline.databinding.ActivityDetailProdukBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailProductActivity : BaseActivity() {
    private lateinit var keranjangRepository: KeranjangRepository

    companion object {
        private const val PRODUK_EXTRA = "extra_produk"
        const val RESULT_DELETE = 10
        fun createIntent(context: Context, data: Produk): Intent {
            return Intent(context, DetailProductActivity::class.java).apply {
                putExtra(PRODUK_EXTRA, data as Parcelable)
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
        keranjangRepository = KeranjangRepository(this)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        verifyProductData()
        initView()
        setContentView(binding.root)
    }

    private fun initView() = with(binding) {
        lifecycleScope.launch {
            userRepository.getRemoteUserData(produkData?.id_users.toString()) { isSuccess, user ->
                if (isSuccess) {
                    sellerPhone.text = user?.noTelepon
                }
            }
        }

        callSeller.setOnClickListener {
            if (sellerPhone.text.isNullOrBlank()) {
                showToast("Nomor WhatsApp tidak ditemukan")
                return@setOnClickListener
            }

            openWhatsApp(
                sellerPhone.text.toString(),
                "Halo, apakah produk ${produkData?.nama} masih ada"
            )
        }

        btnKeranjang.setOnClickListener {
            if (produkData == null) {
                return@setOnClickListener
            }

            showProgressDialog()
            userRepository.uid?.let { uuid ->
                keranjangRepository.addKeranjang(
                    userUid = uuid,
                    produk = produkData!!.toProdukKeranjang(),
                    onComplete = { success ->
                        dismissProgressDialog()
                        if (success) {
                            showToast("berhasil ditambahkan ke keranjang")
                        } else {
                            showToast("gagal ditambahkan ke keranjang")
                        }
                    })
            }
        }

        btnBeli.setOnClickListener {
            startActivity(Intent(this@DetailProductActivity, CheckoutActivity::class.java))
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