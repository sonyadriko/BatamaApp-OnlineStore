package com.example.tokoonline.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.firebase.Produk
import com.example.tokoonline.data.repository.firebase.KeranjangRepository
import com.example.tokoonline.databinding.ActivityDetailProdukBinding
import com.example.tokoonline.databinding.ProductOrderNowBottomSheetBinding
import com.example.tokoonline.view.viewmodel.DetailProdukViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class DetailProductActivity : BaseActivity() {
    private lateinit var keranjangRepository: KeranjangRepository

    private val bottomSheetBinding: ProductOrderNowBottomSheetBinding by lazy {
        ProductOrderNowBottomSheetBinding.inflate(layoutInflater)
    }

    private val vm: DetailProdukViewModel by viewModels()

    private val dialog: BottomSheetDialog by lazy {
        BottomSheetDialog(
            this, R.style.AppTheme_BottomSheetDialog
        )
    }

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

        binding.toolbar.binding.leftIcon.setOnClickListener {
            finish()
        }

        binding.toolbar.binding.rightIcon.setOnClickListener {
            startActivity(Intent(this, KeranjangActivity::class.java))
        }

        binding.lifecycleOwner = this
        bottomSheetBinding.lifecycleOwner = this

        setContentView(binding.root)

        verifyProductData()
        initView()
    }

    private fun initView() = with(binding) {
        lifecycleScope.launch {
            userRepository.getRemoteUserData(produkData?.idSeller.toString()) { isSuccess, user ->
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
                sellerPhone.text.toString(), "Halo, apakah produk ${produkData?.nama} masih ada"
            )
        }
        stok.text = "${produkData?.stok ?: 0} Pcs"
        berat.text = "${produkData?.beratProduk ?: 0} Kg"
        btnKeranjang.setOnClickListener {
            if (produkData == null) {
                return@setOnClickListener
            }
            showProgressDialog()
            userRepository.uid?.let { uuid ->
                keranjangRepository.addKeranjang(
                    userUid = uuid,
                    produk = produkData!!.toProdukKeranjang(vm.quantity.value ?: 1),
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
            showUpdateStatusDialog()
        }
    }

    private fun showUpdateStatusDialog() {
        // set view to dialog & create dialog from the inflated layout
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.apply {
            bottomSheetBinding.viewModel = vm

            tvPrice.text = "Rp. ${produkData!!.harga}"
            tvStok.text = "Stok: ${produkData!!.stok}"
            Glide.with(imgCover)
                .load(produkData!!.image.toUri())
                .placeholder(R.drawable.product)
                .into(imgCover)

            // show dialog
            dialog.window?.run {
                setBackgroundDrawable(ColorDrawable(0))
                dialog.show()
            }

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            btnPlus.setOnClickListener {
                if (produkData!!.stok >= vm.quantity.value!!.plus(1)) {
                    vm.updateQuantity(vm.quantity.value!!.plus(1))
                }
            }

            btnMinus.setOnClickListener {
                if ((vm.quantity.value ?: 0) > 1) {
                    vm.updateQuantity(vm.quantity.value!!.minus(1))
                }
            }

            btnBeli.setOnClickListener {
                startActivity(
                    OrderConfirmationActivity.createIntent(
                        this@DetailProductActivity,
                        arrayOf(produkData!!.toProdukKeranjang(vm.quantity.value ?: 1))
                    )
                )
            }
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
        Glide.with(image).load(produkData.image.toUri()).into(image)

        // dismiss
        dismissProgressDialog()
    }
}