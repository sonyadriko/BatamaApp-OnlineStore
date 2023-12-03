package com.example.tokoonline.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import com.example.tokoonline.BuildConfig
import com.example.tokoonline.R
import com.example.tokoonline.core.base.BaseActivity
import com.example.tokoonline.core.util.getFormattedTimeMidtrans
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.snap.Gopay
import com.midtrans.sdk.corekit.models.snap.Shopeepay
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.uikit.api.model.CustomerDetails as CD
import com.midtrans.sdk.corekit.models.ExpiryModel
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.api.model.Authentication
import com.midtrans.sdk.uikit.api.model.BankTransferRequest
import com.midtrans.sdk.uikit.api.model.BankType
import com.midtrans.sdk.uikit.api.model.CreditCard
import com.midtrans.sdk.uikit.api.model.Expiry
import com.midtrans.sdk.uikit.api.model.PaymentType
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import timber.log.Timber
import java.util.UUID


class CheckoutActivity : BaseActivity() {

    @Suppress("UNCHECKED_CAST")
    private val produkExtra: Pair<String, Long> by lazy {
        intent.getSerializableExtra(DATA_EXTRA) as Pair<String, Long>
    }

    companion object {
        private const val CLIENT_KEY = BuildConfig.CLIENT_KEY
        private const val SERVER_URL = BuildConfig.SERVER_URL

        // extras
        private const val DATA_EXTRA = "extra_data_produk"

        fun createIntent(
            context: Context,
            idProduk: String,
            price: Long,
        ): Intent {
            return Intent(context, CheckoutActivity::class.java).apply {
                putExtra(DATA_EXTRA, idProduk to price)
            }
        }
    }

    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTransactionFlow()
        finish()
    }

    private val itemDetailList: ArrayList<ItemDetails> by lazy {
        arrayListOf(
            ItemDetails("test-01", 36500.0, 1, userRepository.nama),
        )
    }

    private fun initTransactionRequest(): TransactionRequest {
        // Create new Transaction Request
        val transactionRequestNew =
            TransactionRequest(System.currentTimeMillis().toString() + "", 36500.0)
        transactionRequestNew.expiry = ExpiryModel().apply {
            startTime = getFormattedTimeMidtrans(System.currentTimeMillis())
            duration = 5
            unit = Expiry.UNIT_MINUTE
        }
        transactionRequestNew.customerDetails = initCustomerDetails()

        transactionRequestNew.itemDetails = itemDetailList
        return transactionRequestNew
    }

    private fun initCustomerDetails(): CustomerDetails {
        return CustomerDetails().apply {
            firstName = userRepository.nama
            email = userRepository.email
            phone = userRepository.phone
            shippingAddress = ShippingAddress().apply {
                address = "Randu Barat 6/10"
                city = "Surabaya"
                postalCode = "60128"
            }
        }
    }

//    private fun startTransactionFlow() {
//        SdkUIFlowBuilder.init()
//            .setContext(this)
//            .setClientKey(CLIENT_KEY)
//            .setMerchantBaseUrl(SERVER_URL)
//            .setLanguage("id")
//            .setTransactionFinishedCallback { transactionResult ->
//                doOnTransactionFinished(transactionResult)
//            }
//            .enableLog(true) // this is to disable logging
//            .buildSDK()
//
//        MidtransSDK.getInstance().transactionRequest = initTransactionRequest()
//        MidtransSDK.getInstance().startPaymentUiFlow(this@CheckoutActivity)
//    }

    private fun startTransactionFlow() {
        try {
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                this,
                launcher,
                SnapTransactionDetail(userRepository.uid!!, 50000.00, "IDR"),
                CD(
                    "budi-6789",
                    "Budi",
                    "Utomo",
                    "budi@utomo.com",
                    "0213213123",
                    null,
                    null
                ), // Customer Details
                itemDetailList, // Item Details
                CreditCard(
                    false,
                    null,
                    Authentication.AUTH_3DS,
                    null,
                    BankType.MANDIRI,
                    null,
                    null,
                    null,
                    null,
                    null
                ), // Credit Card
                userRepository.uid, // User Id
                null,
                null,
                null,
                Expiry(
                    getFormattedTimeMidtrans(System.currentTimeMillis()),
                    Expiry.UNIT_HOUR,
                    5
                ), // expiry (null: default expiry time)
                PaymentMethod.CREDIT_CARD, // Direct Payment Method Type
                listOf(
                    PaymentType.KLIK_BCA,
                    PaymentType.INDOMARET,
                    PaymentType.BRI_VA
                ), // Enabled Payment (null: enabled all available payment)
                BankTransferRequest(vaNumber = "1234567890"), // Permata Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BCA Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BNI Custom VA (null: default va)
                BankTransferRequest(vaNumber = "12345"), // BRI Custom VA (null: default va)
                "Cash1", // Custom Field 1
                "Debit2", // Custom Field 2
                "Credit3"  // Custom Field 3
            )
        } catch (e : Exception) {
            showToast(e.cause?.message.toString())
        }
    }

    private fun doOnTransactionFinished(transactionResult: com.midtrans.sdk.corekit.models.snap.TransactionResult?) {
        if (transactionResult != null) {
            when (transactionResult.status) {
                STATUS_SUCCESS -> {
                    Timber.d(
                        "Transaction Finished. ID: " + transactionResult.response.transactionId
                    )
                }

                STATUS_PENDING -> {
                    Timber.d(
                        "Transaction Pending. ID: " + transactionResult.response.transactionId
                    )
                }

                STATUS_FAILED -> {
                    Timber.d(
                        "Transaction Failed. ID: " + transactionResult.response.transactionId
                    )
                }

                STATUS_INVALID -> {
                    Timber.d(
                        "Transaction Invalid. ID: " + transactionResult.response.transactionId
                    )
                }

                else -> {
                    Timber.d(
                        "Transaction ID: " + transactionResult.response.transactionId + ". Message: " + transactionResult.status
                    )
                }
            }
        } else {
            Timber.d("Transaction Invalid")
        }
    }
}