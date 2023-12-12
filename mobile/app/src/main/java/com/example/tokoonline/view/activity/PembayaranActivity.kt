package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.tokoonline.BuildConfig
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getTotalBelanja
import com.example.tokoonline.core.util.parcelable
import com.example.tokoonline.core.util.toItemDetails
import com.example.tokoonline.data.model.firebase.ProdukKeranjang
import com.example.tokoonline.data.model.firebase.Transaction
import com.example.tokoonline.data.repository.firebase.AlamatRepository
import com.example.tokoonline.data.repository.firebase.TransactionRepository
import com.example.tokoonline.databinding.ActivityPembayaranBinding
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.api.model.Address
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.coroutines.launch
import java.util.UUID

class PembayaranActivity : BaseActivity() {
    private val alamatRepository: AlamatRepository = AlamatRepository.getInstance()
    private val transactionRepository: TransactionRepository = TransactionRepository.getInstance()

    private lateinit var transaction: Transaction

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

    private lateinit var binding: ActivityPembayaranBinding

    private lateinit var produkKeranjang: Array<ProdukKeranjang>

    private var metodePengiriman: Int? = null

    private lateinit var customerDetails: CustomerDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPembayaranBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

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
            finish()
            showToast(getString(R.string.something_wrong))
        }

        showProgressDialog()
        lifecycleScope.launch {
            alamatRepository.getAlamatByDefault(userRepository.uid!!) {
                dismissProgressDialog()
                if (it == null) {
                    showToast("Kesalahan dalam mendapatkan informasi alamat")
                    finish()
                } else {
                    customerDetails = CustomerDetails(
                        firstName = userRepository.nama,
                        email = userRepository.email,
                        phone = userRepository.phone,
                        shippingAddress = Address(
                            address = it.alamat,
                            city = "Surabaya",
                        ),
                    )
                }
            }
        }

        buildUiKit()
        binding.btnBayar.setOnClickListener {
            startMidtransPayment()
        }

        setContentView(binding.root)
    }

    private fun initTransactionDetails(): SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = UUID.randomUUID().toString(),
            grossAmount = produkKeranjang.getTotalBelanja().toDouble()
        )
    }

    private val snapTrxDetail: SnapTransactionDetail by lazy {
        initTransactionDetails()
    }

    private fun startMidtransPayment() {
        val produk1 = produkKeranjang[0]
        transaction = Transaction(
            nama = produk1.nama,
            id = snapTrxDetail.orderId,
            jumlah = produk1.qty,
            harga = produk1.harga.toDouble(),
            produkId = produk1.produkId,
            status = "MENUNGGU"
        )

        showProgressDialog()
        transactionRepository.addTransaction(transaction) { isComplete ->
            dismissProgressDialog()
            if (isComplete) {
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    activity = this,
                    launcher = launcher,
                    transactionDetails = snapTrxDetail,
                    customerDetails = customerDetails,
                    itemDetails = produkKeranjang!!.toItemDetails()
                )
            } else {
                finish()
                showToast(getString(R.string.something_wrong))
            }
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(BuildConfig.SERVER_URL)
            .withMerchantClientKey(BuildConfig.CLIENT_KEY)
            .enableLog(true)
            .withColorTheme(
                CustomColorTheme(
                    colorPrimaryHex = "#009FE3",
                    colorPrimaryDarkHex = "#009FE3",
                    colorSecondaryHex = "#FF03DAC5",
                )
            )
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }
}