package com.example.tokoonline.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.Result
import com.example.tokoonline.core.util.getFormattedTimeMidtrans
import com.example.tokoonline.core.util.getTotalBelanja
import com.example.tokoonline.core.util.gone
import com.example.tokoonline.core.util.moneyFormatter
import com.example.tokoonline.core.util.visible
import com.example.tokoonline.data.model.firebase.Alamat
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.model.midtrans.BillingAddress
import com.example.tokoonline.data.model.midtrans.CustomerDetails
import com.example.tokoonline.data.model.midtrans.ItemDetailsItem
import com.example.tokoonline.data.model.midtrans.ShippingAddress
import com.example.tokoonline.data.model.midtrans.SnapTokenResponse
import com.example.tokoonline.data.model.midtrans.SnapTransactionDetailRequest
import com.example.tokoonline.data.model.midtrans.TransactionDetails
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.data.repository.midtrans.MidtransRepository
import com.example.tokoonline.databinding.ActivityOrderConfirmationBinding
import com.example.tokoonline.view.adapter.AdapterListProduk
import com.example.tokoonline.view.viewmodel.AlamatViewModel
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class OrderConfirmationActivity : BaseActivity() {
    companion object {
        private const val EXTRA_DATA_PRODUK = "data_produk_extra"
        fun createIntent(context: Context, produkKeranjang: Array<ProdukKeranjang>): Intent {
            return Intent(context, OrderConfirmationActivity::class.java).apply {
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

    private lateinit var binding: ActivityOrderConfirmationBinding
    private lateinit var viewModelAlamat: AlamatViewModel

    @Inject
    lateinit var midtransRepository: MidtransRepository
    private val transactionRepository: TransactionRepository = TransactionRepository.getInstance()

    private var alamat: Alamat? = null

    private var metodePengiriman: String? = null
    private var metodePembayaran: String? = null
    private lateinit var adapterListProduk: AdapterListProduk

    private lateinit var customerDetail: CustomerDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderConfirmationBinding.inflate(layoutInflater)
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
        binding.tvOngkir.text = moneyFormatter(0)
        binding.tvTotal.text = moneyFormatter(produkKeranjang.getTotalBelanja() + 0)


        binding.btnBayar.setOnClickListener {
            if (alamat == null) {
                showToast("tambahkan alamat pengiriman terlebih dahulu")
            } else if (metodePengiriman == null) {
                showToast("tolong pilih metode pengiriman terlebih dahulu")
            } else if (metodePembayaran == null) {
                showToast("tolong pilih metode pembayaran terlebih dahulu")
            } else {
                it.isEnabled = false
                createTransaction()
            }
        }

        binding.kirim.setOnClickListener {
            metodePengiriman = "kirim"
            binding.ambil.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.kirim.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }

        binding.ambil.setOnClickListener {
            metodePengiriman = "ambil"
            binding.kirim.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.ambil.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }


        binding.cod.setOnClickListener {
            metodePembayaran = "cod"
            binding.transfer.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.cod.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }

        binding.transfer.setOnClickListener {
            metodePembayaran = "transfer"
            binding.cod.setBackgroundResource(R.drawable.background_white_radius4_border_grey)
            binding.transfer.setBackgroundResource(R.drawable.background_blue_radius4_border_primary)
        }

        adapterListProduk = AdapterListProduk(produkKeranjang!!.toList())
        binding.detailItem.adapter = adapterListProduk
    }

    private var idAlamat = ""
    @SuppressLint("SetTextI18n")
    fun showAlamatDefault(userUid: String) {
        viewModelAlamat.getAlamatDefault(userUid) { alamatDef ->
            with(binding) {
                if (alamatDef != null) {
                    alamatPlaceholder.gone()
                    alamatDefault.visible()
                    alamat = alamatDef

                    alamatDefault.text =
                        "${alamatDef.nama} \u2022 ${alamatDef.phone}\n${alamatDef.alamat}"
                    idAlamat = alamatDef.id.toString()
                } else alamat = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        val userId = userRepository.uid.toString()
        showAlamatDefault(userId)
        lifecycleScope.launch {
            delay(200)
            dismissProgressDialog()
        }
    }

    private fun initTransactionDetails(): SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = produkKeranjang.getTotalBelanja().toDouble()
        )
    }

    private fun createTransaction() {
        showProgressDialog()

        val shippingAddress = ShippingAddress(
            address = alamat!!.alamat,
            city = "Surabaya",
            countryCode = "IDN",
            phone = userRepository.phone!!,
            lastName = "",
            firstName = userRepository.nama!!,
            email = userRepository.email!!,
            postalCode = "60245"
        )

        val billingAddress = BillingAddress(
            address = alamat!!.alamat,
            city = "Surabaya",
            countryCode = "IDN",
            phone = userRepository.phone!!,
            lastName = "",
            firstName = userRepository.nama!!,
            email = userRepository.email!!,
            postalCode = "60245"
        )

        customerDetail = CustomerDetails(
            firstName = userRepository.nama!!,
            email = userRepository.email!!,
            phone = userRepository.phone!!,
            shippingAddress = shippingAddress,
            billingAddress = billingAddress,
            lastName = ""
        )

        val itemDetails = produkKeranjang!!.map {
            ItemDetailsItem(
                id = it.produkId,
                price = it.harga.toDouble(),
                quantity = it.qty,
                name = it.nama,
                category = "",
                brand = "",
                url = "",
                merchantName = "Batama"
            )
        }

        if (metodePembayaran.equals("cod", ignoreCase = true)) {
            startTransaction(getTransactionTransactionList())
            return
        }

        lifecycleScope.launch {
            midtransRepository.postSnapToken(
                SnapTransactionDetailRequest(
                    customerDetails = customerDetail,
                    itemDetails = itemDetails,
                    transactionDetails = TransactionDetails(
                        orderId = initTransactionDetails().orderId,
                        grossAmount = initTransactionDetails().grossAmount,
                    )
                )
            ).collect { result ->
                when (result) {
                    Result.Loading -> showProgressDialog()
                    is Result.Success -> {
                        binding.btnBayar.isEnabled = true
                        startTransaction(getTransactionTransactionList(result))
                    }

                    is Result.Error -> {
                        binding.btnBayar.isEnabled = true
                        dismissProgressDialog()
                        Timber.e(result.throwable)
                        // showToast("${result.httpCode} : ${result.throwable.message}") //only for testing
                        showDefaultErrorToast()
                    }
                }
            }
        }
    }

    private fun startTransaction(transactions: List<Transaction>) {
        runBlocking {
            withContext(Dispatchers.IO) {
                transactions.forEachIndexed { index, transaction ->
                    try {
                        transactionRepository.addTransaction(
                            transaction.listOfProdukKeranjang.toTypedArray(),
                            transaction
                        ) { isComplete ->
                            dismissProgressDialog()
                            if (index == transactions.size - 1) {
                                if (isComplete) {
                                    startActivity(
                                        Intent(
                                            this@OrderConfirmationActivity,
                                            SuccessOrderActivity::class.java
                                        )
                                    )
                                } else {
                                    throw Exception("error during creating transaction")
                                }
                            } else return@addTransaction
                        }
                    } catch (e: Exception) {
                        e.message?.let { showToast(it) }
                    }
                }
            }
        }
    }

     private fun getTransactionTransactionList(): List<Transaction>  {
         return produkKeranjang!!.groupBy { it.idSeller }.map { product ->
             Transaction(
                 alamatId = idAlamat,
                 orderId = initTransactionDetails().orderId,
                 harga = product.value.sumOf { it.harga * it.qty }.toDouble(),
                 status = "pending",
                 userId = userRepository.uid!!,
                 catatan = binding.edtCatatan.text.toString(),
                 metodePembayaran = metodePembayaran!!,
                 metodePengiriman = metodePengiriman!!,
                 snapToken = "",
                 createdAt = getFormattedTimeMidtrans(System.currentTimeMillis()),
                 idSeller = product.value[0].idSeller!! // id seller is the same
             ).also {
                 it.setProdukKeranjang(product.value)
             }
         }
    }

     private fun getTransactionTransactionList(result: Result.Success<SnapTokenResponse>): List<Transaction>  {
         return produkKeranjang!!.groupBy { it.idSeller }.map { product ->
             Transaction(
                 alamatId = idAlamat,
                 orderId = initTransactionDetails().orderId,
                 harga = product.value.sumOf { it.harga * it.qty }.toDouble(),
                 status = "pending",
                 userId = userRepository.uid!!,
                 catatan = binding.edtCatatan.text.toString(),
                 metodePembayaran = metodePembayaran!!,
                 metodePengiriman = metodePengiriman!!,
                 snapToken = result.data.token,
                 createdAt = getFormattedTimeMidtrans(System.currentTimeMillis()),
                 idSeller = product.value[0].idSeller!! // id seller is the same
             ).also {
                 it.setProdukKeranjang(product.value)
             }
         }
    }
}