package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.Result
import com.example.tokoonline.core.util.getTotalBelanja
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.model.midtrans.BillingAddress
import com.example.tokoonline.data.model.midtrans.CustomerDetails
import com.example.tokoonline.data.model.midtrans.ItemDetailsItem
import com.example.tokoonline.data.model.midtrans.ShippingAddress
import com.example.tokoonline.data.model.midtrans.SnapTokenResponse
import com.example.tokoonline.data.model.midtrans.SnapTransactionDetailRequest
import com.example.tokoonline.data.model.midtrans.TransactionDetails
import com.example.tokoonline.data.repository.firebase.AlamatRepository
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.data.repository.midtrans.MidtransRepository
import com.example.tokoonline.databinding.ActivityPembayaranBinding
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PembayaranActivity : BaseActivity() {
    companion object {
        private const val EXTRA_DATA_PRODUK = "data_produk_extra"
        private const val EXTRA_DATA_PENGIRIMAN = "data_pengiriman_extra"
        fun createIntent(
            context: Context,
            produkKeranjang: Array<ProdukKeranjang>,
            metodePengiriman: Int
        ): Intent {
            return Intent(context, PembayaranActivity::class.java).apply {
                putExtra(EXTRA_DATA_PRODUK, produkKeranjang)
                putExtra(EXTRA_DATA_PENGIRIMAN, metodePengiriman)
            }
        }
    }

    @Inject
    lateinit var midtransRepository: MidtransRepository

    private val alamatRepository: AlamatRepository = AlamatRepository.getInstance()
    private val transactionRepository: TransactionRepository = TransactionRepository.getInstance()

    private var metodePengiriman: Int? = null
    private lateinit var transaction: Transaction
    private lateinit var customerDetail: CustomerDetails
    private lateinit var produkKeranjang: Array<ProdukKeranjang>

    private lateinit var binding: ActivityPembayaranBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        showProgressDialog()
        initExtrasData()
        initAlamatData()

        binding.btnBayar.setOnClickListener {
            pay()
        }

        setContentView(binding.root)
    }

    private fun pay() {
        val itemDetails = produkKeranjang.map {
            ItemDetailsItem(
                id = it.id,
                price = it.harga.toDouble(),
                quantity = it.qty,
                name = it.nama,
                category = "",
                brand = "",
                url = "",
                merchantName = ""
            )
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
                    is Result.Success -> startTransaction(result)
                    is Result.Error -> {
                        dismissProgressDialog()
                        Timber.e(result.throwable)
                        showToast("${result.httpCode} : ${result.throwable.message}")
                    }
                }
            }
        }
    }

    private fun startTransaction(result: Result.Success<SnapTokenResponse>) {
        val product = produkKeranjang[0]
        transaction = Transaction(
            nama = product.nama,
            id = initTransactionDetails().orderId,
            jumlah = product.qty,
            harga = product.harga.toDouble(),
            produkId = product.produkId,
            status = "MENUNGGU"
        )

        transactionRepository.addTransaction(transaction) { isComplete ->
            dismissProgressDialog()
            if (isComplete) {
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    activity = this@PembayaranActivity,
                    launcher = launcher,
                    snapToken = result.data.token,
                )
            } else {
                finish()
                showToast(getString(R.string.something_wrong))
            }
        }
    }

    private fun initAlamatData() {
        lifecycleScope.launch {
            alamatRepository.getAlamatByDefault(userRepository.uid!!) {
                dismissProgressDialog()
                if (it == null) {
                    showToast("Kesalahan dalam mendapatkan informasi alamat")
                    finish()
                } else {
                    val shippingAddress = ShippingAddress(
                        address = it.alamat,
                        city = "Surabaya",
                        countryCode = "62",
                        phone = "",
                        lastName = "",
                        firstName = "",
                        email = "",
                        postalCode = ""
                    )

                    val billingAddress = BillingAddress(
                        address = it.alamat,
                        city = "Surabaya",
                        countryCode = "62",
                        phone = "",
                        lastName = "",
                        firstName = "",
                        email = "",
                        postalCode = ""
                    )

                    customerDetail = CustomerDetails(
                        firstName = userRepository.nama!!,
                        email = userRepository.email!!,
                        phone = userRepository.phone!!,
                        shippingAddress = shippingAddress,
                        billingAddress = billingAddress,
                        lastName = ""
                    )
                }
            }
        }
    }

    private fun initExtrasData() {
        produkKeranjang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelableArray(EXTRA_DATA_PRODUK, ProdukKeranjang::class.java)
                ?: emptyArray()
        } else {
            val a = intent.extras?.getParcelableArray(EXTRA_DATA_PRODUK)
            a?.map { it as ProdukKeranjang }?.toTypedArray() ?: emptyArray()
        }

        metodePengiriman = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getInt(EXTRA_DATA_PENGIRIMAN, 0)
        } else {
            intent.extras?.getInt(EXTRA_DATA_PENGIRIMAN)
        }

        if (produkKeranjang.isEmpty()) {
            dismissProgressDialog()
            showToast(getString(R.string.something_wrong))
            finish()
        }
    }

    private fun initTransactionDetails(): SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = produkKeranjang.getTotalBelanja().toDouble()
        )
    }

    private fun updateStatus(status: String) {
        showProgressDialog()

        val attempt = 3
        var fold = 1
        while (attempt >= fold) {
            transactionRepository.addTransaction(transaction.copy(status = status)) { result ->
                if (result) {
                    dismissProgressDialog()
                    finish()
                    fold = 99
                } else if (fold == attempt) { // last attempt
                    dismissProgressDialog()
                    showToast("gagal mengubah status transaksi, mohon hubungi support batama")
                    finish()
                }
            }
            fold++
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        it.parcelable<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)

                    if (transactionResult != null) {
                        when (transactionResult.status) {
                            UiKitConstants.STATUS_SUCCESS -> {
                                Toast.makeText(
                                    this,
                                    "Transaction Finished. ID: " + transactionResult.transactionId,
                                    Toast.LENGTH_LONG
                                ).show()
                                updateStatus("SUKSES")
                            }

                            UiKitConstants.STATUS_PENDING -> {
                                Toast.makeText(
                                    this,
                                    "Transaction Pending. ID: " + transactionResult.transactionId,
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            UiKitConstants.STATUS_FAILED -> {
                                Toast.makeText(
                                    this,
                                    "Transaction Failed. ID: " + transactionResult.transactionId,
                                    Toast.LENGTH_LONG
                                ).show()
                                updateStatus("BATAL")
                            }

                            UiKitConstants.STATUS_CANCELED -> {
                                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG)
                                    .show()
                                updateStatus("BATAL")
                            }

                            UiKitConstants.STATUS_INVALID -> {
                                Toast.makeText(
                                    this,
                                    "Transaction Invalid. ID: " + transactionResult.transactionId,
                                    Toast.LENGTH_LONG
                                ).show()
                                updateStatus("BATAL")
                            }

                            else -> {
                                Toast.makeText(
                                    this,
                                    "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
                        updateStatus("BATAL")
                    }
                }
            }
        }
}